package com.jogo.adivinhacao.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private String jogador;

    @Column(nullable = false)
    private int perguntasCount;

    private Integer pontos;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    public Partida() {}

    public Partida(String jogador, Long raizId, Long noAtualId) {
        this.jogador = jogador;
        this.raizId = raizId;
        this.noAtualId = noAtualId;
        this.perguntasCount = 0;
        this.criadoEm = LocalDateTime.now();
    }

    public String getId() { return id; }
    public Long getRaizId() { return raizId; }
    public Long getNoAtualId() { return noAtualId; }
    public String getJogador() { return jogador; }
    public int getPerguntasCount() { return perguntasCount; }
    public Integer getPontos() { return pontos; }
    public LocalDateTime getCriadoEm() { return criadoEm; }

    public void setNoAtualId(Long noAtualId) { this.noAtualId = noAtualId; }
    public void setPerguntasCount(int perguntasCount) { this.perguntasCount = perguntasCount; }
    public void setPontos(Integer pontos) { this.pontos = pontos; }
}
