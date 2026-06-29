# Jogo de Adivinhação de Animais — API

API REST em Spring Boot que implementa o jogo de adivinhação com árvore de decisão.

## Como rodar

```bash
./mvnw spring-boot:run
```

O servidor sobe em `http://localhost:8080`.

Console H2 disponível em `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:jogodb`
- User: `sa` / Password: (vazio)

---

## Endpoints

### 1. Iniciar partida

```bash
curl -X POST http://localhost:8080/partida
```

Resposta:
```json
{
  "tipo": "pergunta",
  "conteudo": "O animal que você pensou late?",
  "partidaId": "550e8400-e29b-41d4-a716-446655440000"
}
```

---

### 2. Responder pergunta

```bash
curl -X POST http://localhost:8080/partida/{id}/resposta \
  -H "Content-Type: application/json" \
  -d '{"resposta": "n"}'
```

Resposta quando ainda há pergunta:
```json
{
  "tipo": "pergunta",
  "conteudo": "O animal que você pensou mia?"
}
```

Resposta quando chega a uma folha:
```json
{
  "tipo": "adivinhacao",
  "conteudo": "Gato"
}
```

---

### 3. Aprender novo animal (quando o programa erra)

```bash
curl -X POST http://localhost:8080/partida/{id}/aprender \
  -H "Content-Type: application/json" \
  -d '{
    "novoAnimal": "Papagaio",
    "novaPergunta": "O animal que você pensou tem penas?",
    "respostaParaNovo": "s"
  }'
```

Resposta: `204 No Content`

---

## Fluxo de uma partida

```
POST /partida                          → recebe partidaId + primeira pergunta
POST /partida/{id}/resposta  (s/n)     → recebe próxima pergunta ou adivinhação
POST /partida/{id}/resposta  (s/n)     → ...navega até folha...
  se tipo = "adivinhacao" e programa errou:
POST /partida/{id}/aprender            → ensina o animal correto
```

## Decisões de design

- **Cada partida tem sua própria árvore**: usuários não compartilham estado.
- **H2 em memória**: estado é perdido ao reiniciar o servidor. Intencional para fins de estudo.
- **noAtualId**: o servidor rastreia onde o jogador está na árvore. O cliente só manda "s" ou "n".
- **204 no /aprender**: o servidor não tem nada útil a retornar após aprender. O cliente decide o que fazer a seguir.
"# jpgo-animais" 
