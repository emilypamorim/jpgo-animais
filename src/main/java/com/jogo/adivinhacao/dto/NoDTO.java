package com.jogo.adivinhacao.dto;

public class NoDTO {

    private Long id;
    private String dado;
    private boolean folha;
    private Long simId;
    private Long naoId;

    public NoDTO() {}

    public NoDTO(Long id, String dado, boolean folha, Long simId, Long naoId) {
        this.id = id;
        this.dado = dado;
        this.folha = folha;
        this.simId = simId;
        this.naoId = naoId;
    }

    public Long getId() { return id; }
    public String getDado() { return dado; }
    public boolean isFolha() { return folha; }
    public Long getSimId() { return simId; }
    public Long getNaoId() { return naoId; }
}
