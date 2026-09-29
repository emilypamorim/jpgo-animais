package com.jogo.adivinhacao.controller;

import com.jogo.adivinhacao.dto.RankingDTO;
import com.jogo.adivinhacao.model.Partida;
import com.jogo.adivinhacao.repository.PartidaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class RankingController {

    private final PartidaRepository partidaRepository;

    public RankingController(PartidaRepository partidaRepository) {
        this.partidaRepository = partidaRepository;
    }

    @GetMapping("/ranking")
    public List<RankingDTO> ranking() {
        List<Partida> partidas = partidaRepository.findByPontosIsNotNull();

        List<String> nomes = new ArrayList<>();
        List<Long> somas = new ArrayList<>();
        List<LocalDateTime> primeiros = new ArrayList<>();

        for (Partida p : partidas) {
            int i = nomes.indexOf(p.getJogador());
            if (i == -1) {
                nomes.add(p.getJogador());
                somas.add(p.getPontos().longValue());
                primeiros.add(p.getCriadoEm());
            } else {
                somas.set(i, somas.get(i) + p.getPontos());
                if (p.getCriadoEm().isBefore(primeiros.get(i))) {
                    primeiros.set(i, p.getCriadoEm());
                }
            }
        }

        List<Integer> ordem = new ArrayList<>();
        for (int i = 0; i < nomes.size(); i++) {
            ordem.add(i);
        }
        ordem.sort((a, b) -> {
            int c = Long.compare(somas.get(b), somas.get(a));
            if (c != 0) {
                return c;
            }
            return primeiros.get(a).compareTo(primeiros.get(b));
        });

        List<RankingDTO> ranking = new ArrayList<>();
        for (int i : ordem) {
            if (ranking.size() == 10) {
                break;
            }
            ranking.add(new RankingDTO(nomes.get(i), somas.get(i)));
        }
        return ranking;
    }
}
