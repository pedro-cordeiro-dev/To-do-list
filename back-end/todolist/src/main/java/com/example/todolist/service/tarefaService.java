package com.example.todolist.service;

import com.example.todolist.dto.request.AtualizarTarefaRequest;
import com.example.todolist.dto.request.criarTarefaRequest;
import com.example.todolist.dto.response.TarefaResponse;
import com.example.todolist.model.tarefa;
import com.example.todolist.repository.TarefaRepository;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class tarefaService {

    private static final Logger logger = LoggerFactory.getLogger(tarefaService.class);

    private final TarefaRepository tarefaRepositorio;
    private final emailService emailService;
    private final TelegramService telegramService;

    public tarefaService(TarefaRepository tarefaRepositorio,
                         emailService emailService,
                         TelegramService telegramService) {
        this.tarefaRepositorio = tarefaRepositorio;
        this.emailService = emailService;
        this.telegramService = telegramService;
    }

    public TarefaResponse criarTarefa(criarTarefaRequest request) {
        tarefa novaTarefa = new tarefa(request.descricao());
        tarefa tarefaSalva = tarefaRepositorio.save(novaTarefa);

        try {
            emailService.enviarEmailTarefaCriada(tarefaSalva);
        } catch (Exception e) {
            logger.error("!!! ERRO CRÍTICO AO TENTAR CHAMAR O EMAILSERVICE (Criação) !!!", e);
        }

        try {
            telegramService.enviarNotificacaoTarefaCriada(tarefaSalva);
        } catch (Exception e) {
            logger.error("!!! ERRO CRÍTICO AO TENTAR CHAMAR O TELEGRAMSERVICE (Criação) !!!", e);
        }

        return TarefaResponse.entidade(tarefaSalva);
    }

    public List<TarefaResponse> listarTarefa() {
        return tarefaRepositorio.findAll()
                .stream()
                .map(TarefaResponse::entidade)
                .collect(Collectors.toList());
    }

    public TarefaResponse atualizarTarefa(Long id, AtualizarTarefaRequest request) {
        tarefa tarefaDoBanco = tarefaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        boolean estavaConcluida = tarefaDoBanco.isStatus();

        tarefaDoBanco.setDescricao(request.descricao());
        tarefaDoBanco.setStatus(request.status());

        tarefa tarefaAtualizada = tarefaRepositorio.save(tarefaDoBanco);

        if (!estavaConcluida && tarefaAtualizada.isStatus()) {

            try {
                emailService.enviarEmailTarefaConcluida(tarefaAtualizada);
            } catch (Exception e) {
                logger.error("!!! ERRO CRÍTICO AO CHAMAR EMAILSERVICE (Conclusão) !!!", e);
            }

            try {
                telegramService.enviarNotificacaoTarefaConcluida(tarefaAtualizada);
            } catch (Exception e) {
                logger.error("!!! ERRO CRÍTICO AO CHAMAR TELEGRAMSERVICE (Conclusão) !!!", e);
            }
        }

        return TarefaResponse.entidade(tarefaAtualizada);
    }

    public void deletarTarefa(Long id) {
        if (!tarefaRepositorio.existsById(id)) {
            throw new RuntimeException("Tarefa não encontrada para deletar");
        }
        tarefaRepositorio.deleteById(id);
    }
}