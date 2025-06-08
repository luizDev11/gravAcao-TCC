package com.recorder.repository;


import com.recorder.controller.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByUsuario_IdUsuario(Long usuarioId);

    Optional<Agendamento> findByIdAndUsuario_IdUsuario(Long id, Long usuarioId);
}

