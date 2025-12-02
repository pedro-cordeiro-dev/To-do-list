package com.example.todolist.service;

import com.example.todolist.model.tarefa;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class TelegramService {

    private static final Logger logger = LoggerFactory.getLogger(TelegramService.class);
    private final RestTemplate restTemplate;
    private final configService configService;

    private static final String SEPARATOR = "\\|\\|\\|";

    @Value("${telegram.bot.token:}")
    private String botToken;

    public TelegramService(RestTemplate restTemplate, configService configService) {
        this.restTemplate = restTemplate;
        this.configService = configService;
    }

    public boolean vincularUltimoUsuario() {
        if (botToken == null || botToken.isEmpty()) {
            logger.error("Token do Bot não configurado!");
            return false;
        }

        String url = "https://api.telegram.org/bot" + botToken + "/getUpdates";

        try {
            ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

            if (response.getBody() == null) return false;

            JsonNode result = response.getBody().get("result");

            if (result.isArray() && result.size() > 0) {
                JsonNode ultimaMensagem = result.get(result.size() - 1);
                long idEncontrado = ultimaMensagem.get("message").get("chat").get("id").asLong();

                String nome = "";
                if (ultimaMensagem.get("message").get("chat").has("first_name")) {
                    nome = ultimaMensagem.get("message").get("chat").get("first_name").asText();
                }

                logger.info("Vinculando usuário Telegram: {} (ID: {})", nome, idEncontrado);

                configService.salvarTelegramId(String.valueOf(idEncontrado));
                enviarMensagem(String.valueOf(idEncontrado), "✅ *Seu Telegram foi vinculado ao sistema com sucesso!*");

                return true;
            }

        } catch (Exception e) {
            logger.error("Erro ao tentar vincular Telegram", e);
        }
        return false;
    }

    @Async
    public void enviarNotificacaoTarefaCriada(tarefa tarefa) {
        Optional<String> chatIdOptional = configService.buscarTelegramId();

        if (chatIdOptional.isEmpty()) return;

        String textoBonito = montarTextoBonito("📝 *Nova Tarefa Criada*", tarefa.getDescricao());
        enviarMensagem(chatIdOptional.get(), textoBonito);
    }

    @Async
    public void enviarNotificacaoTarefaConcluida(tarefa tarefa) {
        Optional<String> chatIdOptional = configService.buscarTelegramId();

        if (chatIdOptional.isEmpty()) return;

        String textoBonito = montarTextoBonito("✅ *Tarefa Concluída*", tarefa.getDescricao());
        enviarMensagem(chatIdOptional.get(), textoBonito);
    }

    private String montarTextoBonito(String titulo, String descricaoCrua) {
        String[] partes = descricaoCrua.split(SEPARATOR);

        String desc = descricaoCrua;
        String data = "";
        String hora = "";

        if (partes.length >= 3) {
            desc = partes[0];

            String[] dataParts = partes[1].split("-");
            if(dataParts.length == 3) {
                data = dataParts[2] + "/" + dataParts[1] + "/" + dataParts[0];
            } else {
                data = partes[1];
            }
            hora = partes[2];
        }

        StringBuilder sb = new StringBuilder();
        sb.append(titulo).append("\n\n");
        sb.append("📌 *O que:* ").append(desc).append("\n");

        if (!data.isEmpty()) {
            sb.append("📅 *Quando:* ").append(data).append(" às ").append(hora).append("\n");
        }

        return sb.toString();
    }

    private void enviarMensagem(String chatId, String texto) {
        try {
            String url = String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s&parse_mode=Markdown",
                    botToken, chatId, texto);

            restTemplate.getForObject(url, String.class);

        } catch (Exception e) {
            logger.error("!!! [Async] FALHA AO ENVIAR TELEGRAM !!!: {}", e.getMessage());
        }
    }
}