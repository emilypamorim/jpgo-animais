package com.jogo.adivinhacao.dto;

public class PassoDTO {

    private Long noId;
    private String resposta;

    public PassoDTO() {}

    public PassoDTO(Long noId, String resposta) {
        this.noId = noId;
        this.resposta = resposta;
    }

    public Long getNoId() { return noId; }
    public String getResposta() { return resposta; }
}
