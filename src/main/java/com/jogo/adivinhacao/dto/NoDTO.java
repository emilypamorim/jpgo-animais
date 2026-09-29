package com.jogo.adivinhacao.dto;

public class NoDTO {

    private Long id;
    private String dado;
    private boolean folha;

    public NoDTO() {}

    public NoDTO(Long id, String dado, boolean folha) {
        this.id = id;
        this.dado = dado;
        this.folha = folha;
    }

    public Long getId() { return id; }
    public String getDado() { return dado; }
    public boolean isFolha() { return folha; }
}
