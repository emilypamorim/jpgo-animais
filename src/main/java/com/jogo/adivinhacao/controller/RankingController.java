package com.jogo.adivinhacao.controller;

import com.jogo.adivinhacao.dto.RankingDTO;
import com.jogo.adivinhacao.repository.PartidaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return partidaRepository.findRanking()
                .stream()
                .limit(10)
                .toList();
    }
}
