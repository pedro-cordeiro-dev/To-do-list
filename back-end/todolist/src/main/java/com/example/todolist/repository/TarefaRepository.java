package com.example.todolist.repository;

import com.example.todolist.model.tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarefaRepository extends JpaRepository<tarefa, Long> {

}
