package com.jogo.adivinhacao.controller;

import com.jogo.adivinhacao.dto.RankingDTO;
import com.jogo.adivinhacao.model.Partida;
import com.jogo.adivinhacao.repository.PartidaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class RankingController {

    private final PartidaRepository partidaRepository;

    public RankingController(PartidaRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }

    @GetMapping("/ranking")
    public List<RankingDTO> ranking() {
        record Acumulado(long pontos, LocalDateTime primeiroJogo) {}
        Map<String, Acumulado> porJogador = new HashMap<>();

        for (Partida p : partidaRepository.findByPontosIsNotNull()) {
            porJogador.merge(
                    p.getJogador(),
                    new Acumulado(p.getPontos(), p.getCriadoEm()),
                    (atual, novo) -> new Acumulado(
                            atual.pontos() + novo.pontos(),
                            atual.primeiroJogo().isBefore(novo.primeiroJogo())
                                    ? atual.primeiroJogo() : novo.primeiroJogo())
            );
        }

        List<RankingDTO> ranking = new ArrayList<>(porJogador.entrySet().stream()
                .sorted(Map.Entry.<String, Acumulado>comparingByValue(
                                (a, b) -> Long.compare(b.pontos(), a.pontos()))
                        .thenComparing(e -> e.getValue().primeiroJogo()))
                .limit(10)
                .map(e -> new RankingDTO(e.getKey(), e.getValue().pontos()))
                .toList());
        return ranking;
    }
}
