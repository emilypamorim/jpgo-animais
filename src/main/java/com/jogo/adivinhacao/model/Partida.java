package com.jogo.adivinhacao.model;

import jakarta.persistence.*;

@Entity
@Table(name = "partidas")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private Long raizId;

    @Column(nullable = false)
    private Long noAtualId;

    public Partida() {}

    public Partida(Long raizId, Long noAtualId) {
        this.raizId = raizId;
        this.noAtualId = noAtualId;
    }

    public String getId() { return id; }
    public Long getRaizId() { return raizId; }
    public Long getNoAtualId() { return noAtualId; }

    public void setNoAtualId(Long noAtualId) { this.noAtualId = noAtualId; }
}
