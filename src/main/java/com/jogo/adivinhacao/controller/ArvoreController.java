package com.jogo.adivinhacao.controller;

import com.jogo.adivinhacao.dto.NoDTO;
import com.jogo.adivinhacao.dto.PassoDTO;
import com.jogo.adivinhacao.dto.PartidaAtivaDTO;
import com.jogo.adivinhacao.model.No;
import com.jogo.adivinhacao.model.Partida;
import com.jogo.adivinhacao.repository.NoRepository;
import com.jogo.adivinhacao.repository.PartidaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class ArvoreController {

    private final NoRepository noRepository;
    private final PartidaRepository partidaRepository;

    public ArvoreController(NoRepository noRepository, PartidaRepository partidaRepository) {
        this.noRepository = noRepository;
        this.partidaRepository = partidaRepository;
    }

    @GetMapping("/arvore")
    public List<NoDTO> arvore() {
        return noRepository.findAll().stream()
                .map(n -> new NoDTO(n.getId(), n.getDado(), n.ehFolha()))
                .toList();
    }

    @GetMapping("/partida/ativa")
    public PartidaAtivaDTO partidaAtiva() {
        Partida partida = partidaRepository
                .findTop1ByOrderByCriadoEmDesc()
                .orElse(null);
        if (partida == null) {
            return new PartidaAtivaDTO(null, List.of());
        }

        List<PassoDTO> passos = new ArrayList<>();
        String caminho = partida.getCaminho();
        if (caminho != null && !caminho.isBlank()) {
            String[] partes = caminho.split(";");
            for (int i = 0; i < partes.length; i++) {
                if (i == 0) {
                    passos.add(new PassoDTO(Long.parseLong(partes[i]), null));
                } else {
                    String[] pedaco = partes[i].split(":");
                    passos.add(new PassoDTO(Long.parseLong(pedaco[0]), pedaco[1]));
                }
            }
        }
        return new PartidaAtivaDTO(partida.getJogador(), passos);
    }
}
