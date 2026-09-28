package com.jogo.adivinhacao.dto;

public class RespostaJogoDTO {

    private String tipo;
    private String conteudo;
    private String partidaId;
    private Integer pontos;

    public RespostaJogoDTO() {}

    public RespostaJogoDTO(String tipo, String conteudo) {
        this.tipo = tipo;
        this.conteudo = conteudo;
    }

    public RespostaJogoDTO(String tipo, String conteudo, String partidaId) {
        this.tipo = tipo;
        this.conteudo = conteudo;
        this.partidaId = partidaId;
    }

    public RespostaJogoDTO(String tipo, String conteudo, Integer pontos) {
        this.tipo = tipo;
        this.conteudo = conteudo;
        this.pontos = pontos;
    }

    public String getTipo() { return tipo; }
    public String getConteudo() { return conteudo; }
    public String getPartidaId() { return partidaId; }
    public Integer getPontos() { return pontos; }
}
