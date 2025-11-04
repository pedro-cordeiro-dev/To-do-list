package com.example.todolist.service;

import com.example.todolist.dto.request.AtualizarTarefaRequest;
import com.example.todolist.dto.request.criarTarefaRequest;
import com.example.todolist.dto.response.TarefaResponse;
import com.example.todolist.model.tarefa;
import com.example.todolist.repository.TarefaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class tarefaService {

    private final TarefaRepository tarefaRepository;

    public tarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public List<TarefaResponse> listarTarefa(){
        return tarefaRepository.findAll().stream().map(TarefaResponse::entidade).collect(Collectors.toList());
    }

    public TarefaResponse criarTarefa(criarTarefaRequest criarTarefa){
        tarefa novaTarefa = new tarefa(criarTarefa.descricao());
        tarefa tarefasalva =  tarefaRepository.save(novaTarefa);
        return TarefaResponse.entidade(tarefasalva);
    }

    public TarefaResponse atualizarTarefa(Long id, AtualizarTarefaRequest AtualizarTarefa){
        tarefa tarefa = tarefaRepository.findById(id).orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        tarefa.setDescricao(AtualizarTarefa.descricao());
        tarefa.setStatus(AtualizarTarefa.status());

        tarefa tarefaAtualizada = tarefaRepository.save(tarefa);
        return TarefaResponse.entidade(tarefaAtualizada);
    }

    public void  deletarTarefa(Long id){
        if(!tarefaRepository.existsById(id)){
            throw new RuntimeException("Tarefa não encontrada com o id" + id);
        } tarefaRepository.deleteById(id);
    }

}
