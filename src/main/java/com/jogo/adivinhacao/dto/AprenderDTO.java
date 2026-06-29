package com.jogo.adivinhacao.dto;

public class AprenderDTO {
    private String novoAnimal;
    private String novaPergunta;
    private String respostaParaNovo;

    public String getNovoAnimal() { return novoAnimal; }
    public String getNovaPergunta() { return novaPergunta; }
    public String getRespostaParaNovo() { return respostaParaNovo; }

    public void setNovoAnimal(String novoAnimal) { this.novoAnimal = novoAnimal; }
    public void setNovaPergunta(String novaPergunta) { this.novaPergunta = novaPergunta; }
    public void setRespostaParaNovo(String respostaParaNovo) { this.respostaParaNovo = respostaParaNovo; }
}
