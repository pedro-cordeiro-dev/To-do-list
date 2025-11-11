package com.example.todolist.service; // Usando o seu package

import com.example.todolist.model.tarefa; // Usando o seu import
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class emailService {

    private static final Logger logger = LoggerFactory.getLogger(emailService.class);

    private final JavaMailSender javaMailSender;
    private final configService configService;
    private final String emailRemetente = "tiusaraiva13@gmail.com";

    public emailService(JavaMailSender javaMailSender, configService configService) {
        this.javaMailSender = javaMailSender;
        this.configService = configService;
    }

    @Async
    public void enviarEmailTarefaCriada(tarefa tarefa) { // ATENÇÃO: Convenção (Tarefa)
        Optional<String> emailDestinoOptional = configService.buscarEmail();

        if (emailDestinoOptional.isEmpty()) {
            logger.warn("[Async] E-mail de destino NÃO configurado. Pulando envio 'TAREFA CRIADA'.");
            return;
        }

        String emailDestino = emailDestinoOptional.get();

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(emailRemetente);
        mensagem.setTo(emailDestino);
        mensagem.setSubject("Nova Tarefa Pendente Criada: " + tarefa.getDescricao());
        mensagem.setText(
                        "Olá,\n\n" +
                        "Uma nova tarefa foi criada e está pendente:\n\n" +
                        "ID: " + tarefa.getId() + "\n" +
                        "Descrição: " + tarefa.getDescricao() + "\n\n" +
                        "Atenciosamente,\n" +
                        "Seu App To-Do List"
        );

        try {
            javaMailSender.send(mensagem);
        } catch (Exception e) {
            logger.error("!!! [Async] FALHA AO ENVIAR E-MAIL 'TAREFA CRIADA' !!!: {}", e.getMessage(), e);
        }
    }

    @Async
    public void enviarEmailTarefaConcluida(tarefa tarefa) {
        Optional<String> emailDestinoOptional = configService.buscarEmail();

        if (emailDestinoOptional.isEmpty()) {
            logger.warn("[Async] E-mail de destino NÃO configurado. Pulando envio 'TAREFA CONCLUÍDA'.");
            return;
        }
        String emailDestino = emailDestinoOptional.get();

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(emailRemetente);
        mensagem.setTo(emailDestino);
        mensagem.setSubject("Tarefa Concluída: " + tarefa.getDescricao());
        mensagem.setText(
                "Olá,\n\n" +
                        "A seguinte tarefa foi marcada como concluída:\n\n" +
                        "ID: " + tarefa.getId() + "\n" +
                        "Descrição: " + tarefa.getDescricao() + "\n\n" +
                        "Parabéns!"
        );

        try {
            javaMailSender.send(mensagem);

        } catch (Exception e) {
            logger.error("!!! [Async] FALHA AO ENVIAR E-MAIL 'TAREFA CONCLUÍDA' !!!: {}", e.getMessage(), e);
        }
    }
}