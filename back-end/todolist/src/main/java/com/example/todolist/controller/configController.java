package com.example.todolist.controller;

import com.example.todolist.dto.request.emailRequest;
import com.example.todolist.service.TelegramService;
import com.example.todolist.service.configService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
public class configController {

    private final configService configService;
    private final TelegramService telegramService;

    public configController(configService configService, TelegramService telegramService) {
        this.configService = configService;
        this.telegramService = telegramService;
    }

    @PutMapping("/email")
    public ResponseEntity<Void> definirEmail(@RequestBody emailRequest emailRequest) {
        configService.salvarEmail(emailRequest.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/telegram/vincular")
    public ResponseEntity<String> vincularTelegram() {
        boolean sucesso = telegramService.vincularUltimoUsuario();

        if (sucesso) {
            return ResponseEntity.ok("Sucesso! O último usuário que falou com o bot foi vinculado.");
        } else {
            return ResponseEntity.badRequest().body("Não encontrei mensagens recentes. Mande um 'Oi' para o bot e tente novamente.");
        }
    }
}