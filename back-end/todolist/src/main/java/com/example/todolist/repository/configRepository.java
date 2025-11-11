package com.example.todolist.repository;

import com.example.todolist.model.config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface configRepository extends JpaRepository<config,Long> {



}
