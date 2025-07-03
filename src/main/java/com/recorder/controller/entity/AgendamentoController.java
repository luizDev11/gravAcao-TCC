package com.recorder.controller.entity;

import com.recorder.dto.AgendamentoDTO;
import com.recorder.repository.AgendamentoRepository;
import com.recorder.repository.UsuarioRepository;
import com.recorder.controller.entity.Agendamento;
import com.recorder.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j // Adiciona automaticamente o logger
@RestController
@RequestMapping("/api/agendamentos2")
@PreAuthorize("hasRole('ROLE_USUARIO')")
public class AgendamentoController {

	private final AgendamentoService agendamentoService;
	private final UsuarioRepository usuarioRepository;
	private final AgendamentoRepository agendamentoRepository;

	public AgendamentoController(AgendamentoService agendamentoService, UsuarioRepository usuarioRepository,
			AgendamentoRepository agendamentoRepository) {
		this.agendamentoRepository = agendamentoRepository;
		this.usuarioRepository = usuarioRepository;
		this.agendamentoService = agendamentoService;
	}

	@PostMapping("/criar2")
	public ResponseEntity<Agendamento> criarAgendamento(@Valid @RequestBody AgendamentoDTO agendamentoDTO,
			Authentication authentication) {
		// Obtém o email do usuário autenticado
		String emailUsuario = authentication.getName();

		// agendamentoService.criarAgendamento(agendamentoDTO, emailUsuario);

		log.info("Iniciando criação de agendamento - Usuário autenticado: {}", authentication.getName());
		log.debug("Dados recebidos no DTO: {}", agendamentoDTO);

		// Cria o agendamento
		Agendamento novoAgendamento = agendamentoService.criarAgendamento(agendamentoDTO, emailUsuario);
		log.info("Agendamento criado com ID: {}", novoAgendamento.getId());

		// Cria a URI para o novo recurso
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(novoAgendamento.getId()).toUri();

		log.info("Agendamento persistido com sucesso. Location: {}", location);

		// Retorna resposta 201 (Created) com a URI do novo recurso
		return ResponseEntity.created(location).body(novoAgendamento);
	}

	@GetMapping("/meus-agendamentos")
	public ResponseEntity<List<Agendamento>> getAgendamentosDoUsuario(
			@AuthenticationPrincipal UserDetails userDetails) {
		String email = userDetails.getUsername();
		List<Agendamento> agendamentos = agendamentoRepository.findByUsuarioEmail(email);
		return ResponseEntity.ok(agendamentos);
	}

	@GetMapping
	public ResponseEntity<List<Agendamento>> getMeusAgendamentos(@AuthenticationPrincipal UserDetails userDetails) {

		// 1. Busca o usuário autenticado
		Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

		// 2. Busca os agendamentos do usuário
		List<Agendamento> agendamentos = agendamentoRepository.findByUsuario(usuario);

		return ResponseEntity.ok(agendamentos);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarAgendamento(@PathVariable Long id,
			@AuthenticationPrincipal UserDetails userDetails) {

		// 1. Buscar o agendamento pelo id
		Optional<Agendamento> agendamentoOpt = agendamentoRepository.findById(id);
		if (agendamentoOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamento não encontrado");
		}

		Agendamento agendamento = agendamentoOpt.get();

		// 2. Buscar usuário logado pelo email do token
		Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

		// 3. Verificar se o agendamento pertence ao usuário logado
		if (!agendamento.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode deletar este agendamento");
		}

		// 4. Deletar o agendamento
		agendamentoRepository.delete(agendamento);

		// 5. Retornar sucesso
		return ResponseEntity.ok().body("Agendamento deletado com sucesso");
	}

}