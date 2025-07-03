package com.recorder.controller.entity;

import com.recorder.controller.entity.enuns.Roles;
import com.recorder.dto.LoginDTO;
import com.recorder.dto.UsuarioDTO;
import com.recorder.dto.UsuarioResponse;
import com.recorder.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@PostMapping("/registrar")
	public ResponseEntity<?> registrar(@Valid @RequestBody UsuarioDTO usuarioDTO, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(result.getFieldErrors().stream()
					.map(error -> error.getDefaultMessage()).collect(Collectors.toList()));
		}

		try {
			usuarioDTO.validar();

			// Converter DTO para entidade garantindo ROLE_USUARIO
			Usuario usuario = new Usuario();
			usuario.setEmail(usuarioDTO.getEmail());
			usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
			usuario.setNome(usuarioDTO.getNome());
			usuario.setRoles(Set.of(Roles.ROLE_USUARIO)); // Garante ROLE_USUARIO

			Usuario usuarioSalvo = usuarioService.registrar(usuarioDTO);

			// Converter para DTO de resposta
			UsuarioResponse response = new UsuarioResponse(usuarioSalvo.getIdUsuario(), usuarioSalvo.getNome(),
					usuarioSalvo.getEmail(), usuarioSalvo.getTelefone(), usuarioSalvo.getCpf(),
					usuarioSalvo.getRoles().stream().map(Roles::getDescricao) // Retorna ROLE_USUARIO
							.collect(Collectors.toList()));

			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
		try {
			Usuario usuario = usuarioService.autenticar(loginDTO.getEmail(), loginDTO.getSenha());
			return ResponseEntity.ok(usuario);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(401).body("Credenciais inválidas");
		}
	}

	// ======== CRUD AQUI ========

	@GetMapping
	public ResponseEntity<List<Usuario>> listarTodos() {
		return ResponseEntity.ok(usuarioService.listarTodos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
		try {
			Usuario usuario = usuarioService.buscarPorId(id);
			return ResponseEntity.ok(usuario);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
		try {
			Usuario atualizado = usuarioService.atualizar(id, usuarioDTO);
			return ResponseEntity.ok(atualizado);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletar(@PathVariable Long id) {
		try {
			usuarioService.deletar(id);
			return ResponseEntity.ok().build();
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
