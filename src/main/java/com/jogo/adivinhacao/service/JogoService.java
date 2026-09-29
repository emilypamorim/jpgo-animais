package com.jogo.adivinhacao.service;

import com.jogo.adivinhacao.dto.AprenderDTO;
import com.jogo.adivinhacao.dto.RespostaJogadorDTO;
import com.jogo.adivinhacao.dto.RespostaJogoDTO;
import com.jogo.adivinhacao.model.No;
import com.jogo.adivinhacao.model.Partida;
import com.jogo.adivinhacao.repository.NoRepository;
import com.jogo.adivinhacao.repository.PartidaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class JogoService {

    private static final int PONTOS_MAXIMOS = 15;
    private static final int PONTOS_ENSINAR = 7;

    private final NoRepository noRepository;
    private final PartidaRepository partidaRepository;
    private final DataSource dataSource;

    private Long raizGlobalId;

    public JogoService(NoRepository noRepository, PartidaRepository partidaRepository, DataSource dataSource) {
        this.noRepository = noRepository;
        this.partidaRepository = partidaRepository;
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void inicializarArvoreGlobal() {
        long total = noRepository.count();

        if (total > 0) {
            raizGlobalId = buscarRaizId();
            return;
        }

        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn,
                    new EncodedResource(new ClassPathResource("db/seed.sql"), StandardCharsets.UTF_8),
                    true, false,
                    ScriptUtils.DEFAULT_COMMENT_PREFIX,
                    ScriptUtils.DEFAULT_STATEMENT_SEPARATOR,
                    ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER,
                    ScriptUtils.DEFAULT_BLOCK_COMMENT_END_DELIMITER);
        } catch (SQLException e) {
            throw new IllegalStateException("Falha ao carregar seed da árvore", e);
        }

        raizGlobalId = buscarRaizId();
    }

    private Long buscarRaizId() {
        return noRepository.findAll()
                .stream()
                .mapToLong(No::getId)
                .min()
                .getAsLong();
    }

    @Transactional
    public RespostaJogoDTO iniciarPartida(RespostaJogadorDTO dto) {
        String jogador = normalizarJogador(dto.getJogador());
        No raiz = buscarNo(raizGlobalId);
        Partida partida = partidaRepository.save(new Partida(jogador, raizGlobalId, raizGlobalId));

        Long pontosAcumulados = partidaRepository.findPontosAcumulados(jogador);
        boolean recorrente = pontosAcumulados != null;
        return new RespostaJogoDTO("pergunta", raiz.getDado(), partida.getId(), recorrente, pontosAcumulados);
    }

    private String normalizarJogador(String bruto) {
        if (bruto == null || bruto.trim().isEmpty()) {
            throw new IllegalStateException("Informe um apelido para jogar.");
        }
        return bruto.trim().toLowerCase();
    }

    @Transactional
    public RespostaJogoDTO responder(String partidaId, String resposta) {
        Partida partida = buscarPartida(partidaId);
        No noAtual = buscarNo(partida.getNoAtualId());

        if (noAtual.ehFolha()) {
            throw new IllegalStateException(
                    "Nó atual é uma folha. Use o endpoint de adivinhação ou aprender."
            );
        }

        Long proximoId = resposta.equalsIgnoreCase("s")
                ? noAtual.getSimId()
                : noAtual.getNaoId();

        No proximo = buscarNo(proximoId);
        partida.setNoAtualId(proximo.getId());
        partida.setPerguntasCount(partida.getPerguntasCount() + 1);
        partida.setCaminho(partida.getCaminho() + ";" + proximo.getId() + ":"
                + (resposta.equalsIgnoreCase("s") ? "s" : "n"));

        Integer pontos = null;
        String tipo = proximo.ehFolha() ? "adivinhacao" : "pergunta";
        if (proximo.ehFolha()) {
            // Pontos provisórios: só entram no placar quando o jogador confirma o acerto
            pontos = Math.max(0, PONTOS_MAXIMOS - partida.getPerguntasCount());
        }
        partidaRepository.save(partida);

        return new RespostaJogoDTO(tipo, proximo.getDado(), pontos);
    }

    @Transactional
    public void confirmarAcerto(String partidaId) {
        Partida partida = buscarPartida(partidaId);
        No noAtual = buscarNo(partida.getNoAtualId());
        if (!noAtual.ehFolha()) {
            throw new IllegalStateException(
                    "A partida não está em uma adivinhação — não é possível confirmar."
            );
        }
        partida.setPontos(Math.max(0, PONTOS_MAXIMOS - partida.getPerguntasCount()));
        partidaRepository.save(partida);
    }


    @Transactional
    public void aprender(String partidaId, AprenderDTO dto) {
        Partida partida = buscarPartida(partidaId);
        No folhaErro = buscarNo(partida.getNoAtualId());

        if (!folhaErro.ehFolha()) {
            throw new IllegalStateException(
                    "O nó atual não é uma folha. Não é possível aprender aqui."
            );
        }

        // Verifica se o animal já existe na árvore
        noRepository.findFolhaByDado(dto.getNovoAnimal()).ifPresent(existente -> {
            throw new IllegalArgumentException(
                    "O animal '" + dto.getNovoAnimal() + "' já existe na árvore."
            );
        });

        String animalAntigo = folhaErro.getDado();

        No novoAnimalNo = noRepository.save(new No(dto.getNovoAnimal()));
        No animalAntigoNo = noRepository.save(new No(animalAntigo));

        folhaErro.setDado(dto.getNovaPergunta());

        if (dto.getRespostaParaNovo().equalsIgnoreCase("s")) {
            folhaErro.setSimId(novoAnimalNo.getId());
            folhaErro.setNaoId(animalAntigoNo.getId());
        } else {
            folhaErro.setSimId(animalAntigoNo.getId());
            folhaErro.setNaoId(novoAnimalNo.getId());
        }

        noRepository.save(folhaErro);

        // Ensinar (desafiar a máquina) pontua com bônus fixo
        partida.setPontos(PONTOS_ENSINAR);
        partida.setNoAtualId(raizGlobalId);
        partida.setCaminho(String.valueOf(raizGlobalId));
        partidaRepository.save(partida);
    }

    private Partida buscarPartida(String id) {
        return partidaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Partida não encontrada: " + id));
    }

    private No buscarNo(Long id) {
        return noRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Nó não encontrado: " + id));
    }
}