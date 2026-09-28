package com.jogo.adivinhacao.dto;

public class RankingDTO {

    private String jogador;
    private Integer pontos;

    public RankingDTO() {}

    public RankingDTO(String jogador, Integer pontos) {
        this.jogador = jogador;
        this.pontos = pontos;
    }

    public String getJogador() { return jogador; }
    public Integer getPontos() { return pontos; }
}
