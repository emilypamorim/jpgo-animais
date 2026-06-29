package com.jogo.adivinhacao.repository;

import com.jogo.adivinhacao.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidaRepository extends JpaRepository<Partida, String> {}
