package com.recorder.controller.entity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@CrossOrigin // Permite todas as origens (apenas para desenvolvimento!)
public class TesteConexaoController {

    @GetMapping("/api/teste-conexao")
    public String testeConexao() {
        return "Conexão com o backend funcionando! " + new Date();
    }
}