package com.recorder.service;

import com.recorder.controller.entity.Agendamento;
import com.recorder.controller.entity.enuns.StatusAgendamento;
import com.recorder.repository.AgendamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repository;

    public AgendamentoService(AgendamentoRepository repository) {
        this.repository = repository;
    }

    public Agendamento salvar(Agendamento agendamento) {
        return repository.save(agendamento);
    }

    public List<Agendamento> listarTodos() {
        return repository.findAll();
    }

    public Optional<Agendamento> buscarOptionalPorId(Long id) {
        return repository.findById(id);
    }

    public Agendamento buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public Agendamento atualizarStatus(Long id, StatusAgendamento status) {  // Renomeie o parâmetro
        Agendamento agendamento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        agendamento.setStatus(status);  // Usa o enum diretamente

        return repository.save(agendamento);
    }
    public List<Agendamento> buscarAgendamentosPorUsuario(Long usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }
        return repository.findByUsuario_IdUsuario(usuarioId);
    }

    public Agendamento buscarAgendamentoDoUsuario(Long id, Long usuarioId) {
        return repository.findByIdAndUsuario_IdUsuario(id, usuarioId)
                .orElseThrow(() -> new RuntimeException(
                        "Agendamento não encontrado com ID: " + id +
                                " para o usuário ID: " + usuarioId));
    }

    public void deletar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Agendamento não encontrado para deletar.");
        }
    }
}
