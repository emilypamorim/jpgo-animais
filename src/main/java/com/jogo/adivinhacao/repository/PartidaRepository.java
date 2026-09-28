package com.jogo.adivinhacao.repository;

import com.jogo.adivinhacao.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, String> {

    List<Partida> findTop10ByPontosIsNotNullOrderByPontosDescCriadoEmAsc();
}
