package com.example.todolist.dto.response;

import com.example.todolist.model.tarefa;

public record TarefaResponse(Long id, String descricao, boolean status) {

    public static TarefaResponse entidade(tarefa tarefa){
        return new TarefaResponse(tarefa.getId(), tarefa.getDescricao(), tarefa.isStatus());
    }

}
