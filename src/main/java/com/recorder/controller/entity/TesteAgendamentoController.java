package com.recorder.controller.entity;

import com.recorder.controller.entity.enuns.StatusAgendamento;
import com.recorder.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/teste-agendamento")
public class TesteAgendamentoController {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    // Endpoint para salvar um agendamento de teste
    @PostMapping("/salvar-teste")
    public String salvarAgendamentoTeste() {
        Agendamento agendamento = new Agendamento();
        agendamento.setDescricao("TESTE PERSISTÊNCIA");
        agendamento.setData(LocalDate.from(LocalDateTime.now()));
        agendamento.setStatus(StatusAgendamento.valueOf("PENDENTE")); // Use o enum/valor correto do seu sistema

        agendamentoRepository.save(agendamento);

        return "Agendamento de teste salvo! Verifique o banco de dados.";
    }

    // Endpoint para listar todos os agendamentos (opcional)
    @GetMapping("/listar")
    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }
}
