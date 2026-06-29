package com.jogo.adivinhacao.service;

import com.jogo.adivinhacao.dto.AprenderDTO;
import com.jogo.adivinhacao.dto.RespostaJogoDTO;
import com.jogo.adivinhacao.model.No;
import com.jogo.adivinhacao.model.Partida;
import com.jogo.adivinhacao.repository.NoRepository;
import com.jogo.adivinhacao.repository.PartidaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JogoService {

    private final NoRepository noRepository;
    private final PartidaRepository partidaRepository;

    private Long raizGlobalId;

    public JogoService(NoRepository noRepository, PartidaRepository partidaRepository) {
        this.noRepository = noRepository;
        this.partidaRepository = partidaRepository;
    }

    @PostConstruct
    @Transactional
    public void inicializarArvoreGlobal() {
        long total = noRepository.count();

        if (total > 0) {
            raizGlobalId = noRepository.findAll()
                    .stream()
                    .mapToLong(No::getId)
                    .min()
                    .getAsLong();
            return;
        }

        No cachorro = noRepository.save(new No("Cachorro"));
        No gato = noRepository.save(new No("Gato"));

        No raiz = new No("O animal que você pensou late?");
        raiz.setSimId(cachorro.getId());
        raiz.setNaoId(gato.getId());
        raiz = noRepository.save(raiz);

        raizGlobalId = raiz.getId();
    }

    @Transactional
    public RespostaJogoDTO iniciarPartida() {
        No raiz = buscarNo(raizGlobalId);
        Partida partida = partidaRepository.save(new Partida(raizGlobalId, raizGlobalId));
        return new RespostaJogoDTO("pergunta", raiz.getDado(), partida.getId());
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
        partidaRepository.save(partida);

        String tipo = proximo.ehFolha() ? "adivinhacao" : "pergunta";
        return new RespostaJogoDTO(tipo, proximo.getDado());
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

        partida.setNoAtualId(raizGlobalId);
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