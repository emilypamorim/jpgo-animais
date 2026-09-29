package com.jogo.adivinhacao.repository;

import com.jogo.adivinhacao.dto.RankingDTO;
import com.jogo.adivinhacao.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, String> {

    @Query("SELECT new com.jogo.adivinhacao.dto.RankingDTO(p.jogador, MAX(p.pontos)) " +
           "FROM Partida p WHERE p.pontos IS NOT NULL " +
           "GROUP BY p.jogador " +
           "ORDER BY MAX(p.pontos) DESC, MIN(p.criadoEm) ASC")
    List<RankingDTO> findRanking();
}
