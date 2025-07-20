package com.recorder.repository;

import com.recorder.controller.entity.Agendamento;
import com.recorder.controller.entity.Usuario;

import java.util.List;

import com.recorder.controller.entity.enuns.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
	List<Agendamento> findByUsuario(Usuario usuario);

	List<Agendamento> findByUsuarioEmail(String email);
	// ✨ NOVO MÉTODO: Buscar agendamentos por status, ordenados pela data/hora ✨
	List<Agendamento> findByStatusOrderByDataAscHorarioAsc(StatusAgendamento status);
}
