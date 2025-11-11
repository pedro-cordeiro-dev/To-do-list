package com.example.todolist.controller;

import com.example.todolist.dto.request.emailRequest;
import com.example.todolist.service.configService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
public class configController {

    private final configService configService;

    public  configController(configService configService) {
        this.configService = configService;
    }

    @PutMapping("/email")
    public ResponseEntity<Void> definirEmail(@RequestBody emailRequest emailRequest) {
        configService.salvarEmail(emailRequest.email());
        return ResponseEntity.ok().build();
    }

}
