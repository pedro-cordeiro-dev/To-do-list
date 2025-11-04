package com.example.todolist.controller;

import com.example.todolist.dto.request.AtualizarTarefaRequest;
import com.example.todolist.dto.request.criarTarefaRequest;
import com.example.todolist.dto.response.TarefaResponse;
import com.example.todolist.service.tarefaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final tarefaService tarefaService;

    public  TarefaController(tarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    public ResponseEntity<TarefaResponse> criarTarefa(@RequestBody criarTarefaRequest criarTarefaRequest){
        TarefaResponse tarefaResponse = tarefaService.criarTarefa(criarTarefaRequest);

        return new ResponseEntity<>(tarefaResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TarefaResponse>> listarTarefa(){
        List<TarefaResponse> tarefaResponse = tarefaService.listarTarefa();
        return ResponseEntity.ok(tarefaResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponse> atualizarTarefa(@PathVariable Long id, @RequestBody AtualizarTarefaRequest atualizarTarefaRequest){
        TarefaResponse TarefaResponse = tarefaService.atualizarTarefa(id, atualizarTarefaRequest);
        return ResponseEntity.ok(TarefaResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TarefaResponse> deletarTarefa(@PathVariable Long id){
        tarefaService.deletarTarefa(id);
        return ResponseEntity.noContent().build();
    }

}
