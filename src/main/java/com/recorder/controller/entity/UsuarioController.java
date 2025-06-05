package com.recorder.controller.entity;
import com.recorder.dto.LoginDTO;
import com.recorder.dto.UsuarioDTO;
import com.recorder.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@Valid @RequestBody UsuarioDTO usuarioDTO, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldErrors()
                            .stream()
                            .map(error -> error.getDefaultMessage())
                            .collect(Collectors.toList()));
        }

        try {
            usuarioDTO.validar();
            Usuario usuarioSalvo = usuarioService.registrar(usuarioDTO);
            return ResponseEntity.ok(usuarioSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            Usuario usuario = usuarioService.autenticar(loginDTO.getEmail(), loginDTO.getSenha());
            return ResponseEntity.ok(usuario); // ou retornar um token JWT se for usar segurança
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }

}