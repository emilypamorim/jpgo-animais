package com.jogo.adivinhacao.dto;

import java.util.List;

public class PartidaAtivaDTO {

    private String jogador;
    private List<PassoDTO> passos;

    public PartidaAtivaDTO() {}

    public PartidaAtivaDTO(String jogador, List<PassoDTO> passos) {
        this.jogador = jogador;
        this.passos = passos;
    }

    public String getJogador() { return jogador; }
    public List<PassoDTO> getPassos() { return passos; }
}
