package com.recorder.controller.entity;

import com.recorder.dto.AgendamentoDTO;
import com.recorder.controller.entity.Agendamento;
import com.recorder.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/agendamentos2")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping("/criar2")
    public ResponseEntity<Agendamento> criarAgendamento(
            @Valid @RequestBody AgendamentoDTO agendamentoDTO,
            Authentication authentication) {

        // Obtém o email do usuário autenticado
        String emailUsuario = authentication.getName();

        // Cria o agendamento
        Agendamento novoAgendamento = agendamentoService.criarAgendamento(agendamentoDTO, emailUsuario);

        // Cria a URI para o novo recurso
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoAgendamento.getId())
                .toUri();

        // Retorna resposta 201 (Created) com a URI do novo recurso
        return ResponseEntity.created(location).body(novoAgendamento);
    }
}