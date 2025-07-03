package com.recorder.repository;

import com.recorder.controller.entity.Agendamento;
import com.recorder.controller.entity.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
	List<Agendamento> findByUsuario(Usuario usuario);

	List<Agendamento> findByUsuarioEmail(String email);
}
