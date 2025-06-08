package com.recorder.controller.entity;


import com.recorder.controller.entity.enuns.StatusAgendamento;
import com.recorder.dto.AgendamentoDTO;
import com.recorder.repository.AgendamentoRepository;
import com.recorder.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@CrossOrigin(origins = "*")
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    private final AgendamentoService agendamentoService;
    private final AgendamentoRepository repository;

    public AgendamentoController(AgendamentoService agendamentoService,
                                 AgendamentoRepository repository) {
        this.agendamentoService = agendamentoService;
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Agendamento> salvar(@RequestBody Agendamento agendamento) {
        Agendamento salvo = repository.save(agendamento);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> listar() {
        List<Agendamento> agendamentos = repository.findAll();
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Agendamento> listarPorUsuario(@PathVariable Long usuarioId) {
        return agendamentoService.buscarAgendamentosPorUsuario(usuarioId);
    }

    @GetMapping("/{id}/usuario/{usuarioId}")
    public Agendamento buscarPorIdEUsuario(
            @PathVariable Long id,
            @PathVariable Long usuarioId) {
        return agendamentoService.buscarAgendamentoDoUsuario(id, usuarioId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Agendamento> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusAgendamento status) {

        try {
            Agendamento atualizado = agendamentoService.atualizarStatus(id, status);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> atualizarAgendamento(
            @PathVariable Long id,
            @RequestBody Agendamento agendamentoAtualizado) {

        if (!id.equals(agendamentoAtualizado.getId())) {
            return ResponseEntity.badRequest().build();
        }

        Agendamento atualizado = repository.save(agendamentoAtualizado);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/api/agendamentos/{id}")
    public ResponseEntity<AgendamentoDTO> buscarDetalhes(@PathVariable Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));

        AgendamentoDTO dto = new AgendamentoDTO();
        dto.setNomeCliente(agendamento.getNome());                 // Vem da própria entidade
        dto.setEmail(agendamento.getEmail());                      // Vem da própria entidade
        dto.setTelefone(agendamento.getTelefone());                // Vem da própria entidade
        dto.setPlano(agendamento.getPlano());
        dto.setEndereco(agendamento.getLocal());
        dto.setData(agendamento.getDataJogo().toString());         // Ex: 2025-06-07
        dto.setHorario(agendamento.getHorario().toString());       // Ex: 14:30
        dto.setStatus(agendamento.getStatus().name());             // Enum convertido para texto

        return ResponseEntity.ok(dto);
    }


}