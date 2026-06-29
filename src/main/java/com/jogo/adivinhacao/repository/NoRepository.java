package com.jogo.adivinhacao.repository;

import com.jogo.adivinhacao.model.No;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NoRepository extends JpaRepository<No, Long> {

    @Query("SELECT n FROM No n WHERE LOWER(n.dado) = LOWER(:dado) AND n.simId IS NULL AND n.naoId IS NULL")
    Optional<No> findFolhaByDado(String dado);
}