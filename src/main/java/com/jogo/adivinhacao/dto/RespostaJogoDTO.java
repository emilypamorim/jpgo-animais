package com.jogo.adivinhacao.dto;

public class RespostaJogoDTO {

    private String tipo;
    private String conteudo;
    private String partidaId;

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

    public String getTipo() { return tipo; }
    public String getConteudo() { return conteudo; }
    public String getPartidaId() { return partidaId; }
}
