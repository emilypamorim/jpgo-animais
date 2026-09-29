package com.jogo.adivinhacao.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RespostaJogoDTO {

    private String tipo;
    private String conteudo;
    private String partidaId;
    private Integer pontos;
    private Boolean jogadorRecorrente;
    private Integer melhorPontos;

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

    public RespostaJogoDTO(String tipo, String conteudo, String partidaId,
                           Boolean jogadorRecorrente, Integer melhorPontos) {
        this.tipo = tipo;
        this.conteudo = conteudo;
        this.partidaId = partidaId;
        this.jogadorRecorrente = jogadorRecorrente;
        this.melhorPontos = melhorPontos;
    }

    public String getTipo() { return tipo; }
    public String getConteudo() { return conteudo; }
    public String getPartidaId() { return partidaId; }
    public Integer getPontos() { return pontos; }
    public Boolean getJogadorRecorrente() { return jogadorRecorrente; }
    public Integer getMelhorPontos() { return melhorPontos; }
}
