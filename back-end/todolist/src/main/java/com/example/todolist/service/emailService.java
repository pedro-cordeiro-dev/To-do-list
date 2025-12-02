package com.example.todolist.service;

import com.example.todolist.model.tarefa;
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

    private static final String SEPARATOR = "\\|\\|\\|";

    public emailService(JavaMailSender javaMailSender, configService configService) {
        this.javaMailSender = javaMailSender;
        this.configService = configService;
    }

    @Async
    public void enviarEmailTarefaCriada(tarefa tarefa) {
        Optional<String> emailDestinoOptional = configService.buscarEmail();

        if (emailDestinoOptional.isEmpty()) {
            logger.warn("[Async] E-mail de destino NÃO configurado. Pulando envio 'TAREFA CRIADA'.");
            return;
        }

        String emailDestino = emailDestinoOptional.get();
        String corpoFormatado = montarTextoEmail(tarefa.getDescricao());

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(emailRemetente);
        mensagem.setTo(emailDestino);
        mensagem.setSubject("Nova Tarefa Criada");
        mensagem.setText(
                "Olá,\n\n" +
                        "Uma nova tarefa foi adicionada à sua lista:\n\n" +
                        corpoFormatado + "\n\n" +
                        "Atenciosamente,\n" +
                        "Seu App TaskMaster\n"
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
        String corpoFormatado = montarTextoEmail(tarefa.getDescricao());

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(emailRemetente);
        mensagem.setTo(emailDestino);
        mensagem.setSubject("Tarefa Concluída!");
        mensagem.setText(
                "Parabéns!\n\n" +
                        "A seguinte tarefa foi marcada como concluída:\n\n" +
                        corpoFormatado + "\n\n" +
                        "Continue assim!"
        );

        try {
            javaMailSender.send(mensagem);
        } catch (Exception e) {
            logger.error("!!! [Async] FALHA AO ENVIAR E-MAIL 'TAREFA CONCLUÍDA' !!!: {}", e.getMessage(), e);
        }
    }

    private String montarTextoEmail(String descricaoCrua) {
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

            return "Descrição: " + desc + "\n" +
                    "Data: " + data + "\n" +
                    "Horário: " + hora;
        }

        return "Descrição: " + desc;
    }
}