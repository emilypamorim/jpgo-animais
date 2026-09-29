package com.jogo.adivinhacao.repository;

import com.jogo.adivinhacao.dto.RankingDTO;
import com.jogo.adivinhacao.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, String> {

    List<Partida> findByPontosIsNotNull();

    java.util.Optional<Partida> findTop1ByOrderByCriadoEmDesc();

    @Query("SELECT SUM(p.pontos) FROM Partida p WHERE p.jogador = :jogador AND p.pontos IS NOT NULL")
    Long findPontosAcumulados(@org.springframework.data.repository.query.Param("jogador") String jogador);
}
