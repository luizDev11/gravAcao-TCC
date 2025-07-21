package com.recorder.controller.entity;

import com.recorder.dto.AgendamentoDTO;
import com.recorder.repository.AgendamentoRepository;
import com.recorder.repository.UsuarioRepository;
import com.recorder.service.AgendamentoService;
import com.recorder.controller.entity.enuns.StatusAgendamento; // ✨ Certifique-se que este import está presente! ✨
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/agendamentos2")
@PreAuthorize("hasAnyRole('ROLE_USUARIO', 'ROLE_PROFISSIONAL', 'ROLE_ADMIN')")
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
	@PreAuthorize("hasRole('ROLE_USUARIO')") // Adicionei esta permissão explícita
	public ResponseEntity<Agendamento> criarAgendamento(@Valid @RequestBody AgendamentoDTO agendamentoDTO,
														Authentication authentication) {
		String emailUsuario = authentication.getName();

		log.info("Iniciando criação de agendamento - Usuário autenticado: {}", authentication.getName());
		log.debug("Dados recebidos no DTO: {}", agendamentoDTO);

		Agendamento novoAgendamento = agendamentoService.criarAgendamento(agendamentoDTO, emailUsuario);
		log.info("Agendamento criado com ID: {}", novoAgendamento.getId());

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(novoAgendamento.getId()).toUri();

		log.info("Agendamento persistido com sucesso. Location: {}", location);

		return ResponseEntity.created(location).body(novoAgendamento);
	}

	@GetMapping("/meus-agendamentos")
	@PreAuthorize("hasRole('ROLE_USUARIO')") // Adicionei esta permissão explícita
	public ResponseEntity<List<Agendamento>> getAgendamentosDoUsuario(
			@AuthenticationPrincipal UserDetails userDetails) {
		String email = userDetails.getUsername();
		List<Agendamento> agendamentos = agendamentoRepository.findByUsuarioEmail(email);
		return ResponseEntity.ok(agendamentos);
	}

	@GetMapping
	@PreAuthorize("hasRole('ROLE_USUARIO')") // Adicionei esta permissão explícita
	public ResponseEntity<List<Agendamento>> getMeusAgendamentos(@AuthenticationPrincipal UserDetails userDetails) {
		Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

		List<Agendamento> agendamentos = agendamentoRepository.findByUsuario(usuario);

		return ResponseEntity.ok(agendamentos);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USUARIO')") // Adicionei esta permissão explícita
	public ResponseEntity<?> deletarAgendamento(@PathVariable Long id,
												@AuthenticationPrincipal UserDetails userDetails) {
		Optional<Agendamento> agendamentoOpt = agendamentoRepository.findById(id);
		if (agendamentoOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agendamento não encontrado");
		}

		Agendamento agendamento = agendamentoOpt.get();

		Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

		if (!agendamento.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não pode deletar este agendamento");
		}

		agendamentoRepository.delete(agendamento);

		return ResponseEntity.ok().body("Agendamento deletado com sucesso");
	}
	// ✨ NOVO MÉTODO: Endpoint para buscar um único agendamento por ID ✨
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ROLE_PROFISSIONAL', 'ROLE_ADMIN')") // Ajuste as roles conforme sua necessidade
	@Operation(summary = "Busca um agendamento específico por ID",
			responses = {
					@ApiResponse(responseCode = "200", description = "Agendamento encontrado com sucesso"),
					@ApiResponse(responseCode = "404", description = "Agendamento não encontrado"),
					@ApiResponse(responseCode = "403", description = "Acesso negado")
			})
	public ResponseEntity<Agendamento> getAgendamentoById(@PathVariable Long id) {
		log.info("Buscando agendamento com ID: {}", id);
		Optional<Agendamento> agendamento = agendamentoService.getAgendamentoById(id);

		if (agendamento.isPresent()) {
			log.info("Agendamento ID {} encontrado.", id);
			return ResponseEntity.ok(agendamento.get());
		} else {
			log.warn("Agendamento ID {} não encontrado.", id);
			return ResponseEntity.notFound().build();
		}
	}
	// --- NOVOS ENDPOINTS PARA PROFISSIONAL ---

	@GetMapping("/pendentes")
	@PreAuthorize("hasAnyRole('ROLE_PROFISSIONAL', 'ROLE_ADMIN')")
	@Operation(summary = "Lista todos os agendamentos pendentes para aceitação/rejeição",
			responses = {
					@ApiResponse(responseCode = "200", description = "Lista de agendamentos pendentes"),
					@ApiResponse(responseCode = "403", description = "Acesso negado")
			})
	public ResponseEntity<List<Agendamento>> getAgendamentosPendentes() {
		// ✨ CORREÇÃO AQUI: Passando o ENUM StatusAgendamento.PENDENTE ✨
		List<Agendamento> agendamentos = agendamentoService.getAgendamentosByStatus(StatusAgendamento.PENDENTE);
		return ResponseEntity.ok(agendamentos);
	}

	@GetMapping("/confirmados") // ✨ Nome do endpoint ajustado para refletir o enum "CONFIRMADO" ✨
	@PreAuthorize("hasAnyRole('ROLE_PROFISSIONAL', 'ROLE_ADMIN')")
	@Operation(summary = "Lista todos os agendamentos confirmados pelo profissional",
			responses = {
					@ApiResponse(responseCode = "200", description = "Lista de agendamentos confirmados"),
					@ApiResponse(responseCode = "403", description = "Acesso negado")
			})
	public ResponseEntity<List<Agendamento>> getAgendamentosConfirmados() { // ✨ Nome do método ajustado ✨
		// ✨ CORREÇÃO AQUI: Passando o ENUM StatusAgendamento.CONFIRMADO ✨
		List<Agendamento> agendamentos = agendamentoService.getAgendamentosByStatus(StatusAgendamento.CONFIRMADO);
		return ResponseEntity.ok(agendamentos);
	}

	@PutMapping("/confirmar/{id}") // ✨ Nome do endpoint ajustado para refletir o enum "CONFIRMADO" ✨
	@PreAuthorize("hasAnyRole('ROLE_PROFISSIONAL', 'ROLE_ADMIN')")
	@Operation(summary = "Confirma um agendamento específico",
			responses = {
					@ApiResponse(responseCode = "200", description = "Agendamento confirmado com sucesso"),
					@ApiResponse(responseCode = "404", description = "Agendamento não encontrado"),
					@ApiResponse(responseCode = "403", description = "Acesso negado ou agendamento já processado")
			})
	public ResponseEntity<Agendamento> confirmarAgendamento(@PathVariable Long id) { // ✨ Nome do método ajustado ✨
		// ✨ CORREÇÃO AQUI: Passando o ENUM StatusAgendamento.CONFIRMADO ✨
		Agendamento agendamentoAtualizado = agendamentoService.updateAgendamentoStatus(id, StatusAgendamento.CONFIRMADO);
		return ResponseEntity.ok(agendamentoAtualizado);
	}

	@PutMapping("/recusar/{id}") // ✨ Nome do endpoint ajustado para refletir o enum "RECUSADO" ✨
	@PreAuthorize("hasAnyRole('ROLE_PROFISSIONAL', 'ROLE_ADMIN')")
	@Operation(summary = "Recusa um agendamento específico",
			responses = {
					@ApiResponse(responseCode = "200", description = "Agendamento recusado com sucesso"),
					@ApiResponse(responseCode = "404", description = "Agendamento não encontrado"),
					@ApiResponse(responseCode = "403", description = "Acesso negado ou agendamento já processado")
			})
	public ResponseEntity<Agendamento> recusarAgendamento(@PathVariable Long id) { // ✨ Nome do método ajustado ✨
		// ✨ CORREÇÃO AQUI: Passando o ENUM StatusAgendamento.RECUSADO ✨
		Agendamento agendamentoAtualizado = agendamentoService.updateAgendamentoStatus(id, StatusAgendamento.RECUSADO);
		return ResponseEntity.ok(agendamentoAtualizado);
	}
}