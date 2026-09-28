package com.jogo.adivinhacao.controller;

import com.jogo.adivinhacao.dto.AprenderDTO;
import com.jogo.adivinhacao.dto.RespostaJogadorDTO;
import com.jogo.adivinhacao.dto.RespostaJogoDTO;
import com.jogo.adivinhacao.service.JogoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/partida")
public class JogoController {

    private final JogoService jogoService;

    public JogoController(JogoService jogoService) {
        this.jogoService = jogoService;
    }

    @PostMapping
    public ResponseEntity<RespostaJogoDTO> iniciarPartida(@RequestBody RespostaJogadorDTO dto) {
        RespostaJogoDTO resposta = jogoService.iniciarPartida(dto);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/{id}/resposta")
    public ResponseEntity<RespostaJogoDTO> responder(
            @PathVariable String id,
            @RequestBody RespostaJogadorDTO dto) {
        RespostaJogoDTO resposta = jogoService.responder(id, dto.getResposta());
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/{id}/aprender")
    public ResponseEntity<Void> aprender(
            @PathVariable String id,
            @RequestBody AprenderDTO dto) {
        jogoService.aprender(id, dto);
        return ResponseEntity.noContent().build();
    }
}
