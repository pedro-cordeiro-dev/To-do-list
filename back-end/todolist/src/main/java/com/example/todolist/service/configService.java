package com.example.todolist.service;

import com.example.todolist.model.config;
import com.example.todolist.repository.configRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class configService {

    private final configRepository configRepository;
    private static final Long CONFIG_ID = 1L;

    public configService(configRepository configRepository) {
        this.configRepository = configRepository;
    }

    public void salvarEmail(String email) {
        config config = configRepository.findById(CONFIG_ID).orElse(new config());
        config.setEmail(email);
        configRepository.save(config);
    }

    public void salvarTelegramId(String telegramId) {
        config config = configRepository.findById(CONFIG_ID).orElse(new config());
        config.setTelegramChatId(telegramId);
        configRepository.save(config);
    }

    public Optional<String> buscarTelegramId() {
        return configRepository.findById(CONFIG_ID)
                .map(config::getTelegramChatId)
                .filter(id -> id != null && !id.isBlank());
    }

    public Optional<String> buscarEmail() {
        return configRepository.findById(CONFIG_ID)
                .map(config::getEmail)
                .filter(email -> email != null && !email.isBlank());
    }
}