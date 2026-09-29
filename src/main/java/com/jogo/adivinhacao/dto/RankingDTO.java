package com.jogo.adivinhacao.dto;

public class RankingDTO {

    private String jogador;
    private Long pontos;

    public RankingDTO() {}

    public RankingDTO(String jogador, Long pontos) {
        this.jogador = jogador;
        this.pontos = pontos;
    }

    public String getJogador() { return jogador; }
    public Integer getPontos() { return pontos; }
}
