package com.jogo.adivinhacao.model;

import jakarta.persistence.*;

@Entity
@Table(name = "nos")
public class No {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String dado;

    private Long simId;

    private Long naoId;

    public No() {}

    public No(String dado) {
        this.dado = dado;
    }

    public boolean ehFolha() {
        return simId == null && naoId == null;
    }

    public Long getId() { return id; }
    public String getDado() { return dado; }
    public Long getSimId() { return simId; }
    public Long getNaoId() { return naoId; }

    public void setDado(String dado) { this.dado = dado; }
    public void setSimId(Long simId) { this.simId = simId; }
    public void setNaoId(Long naoId) { this.naoId = naoId; }
}
