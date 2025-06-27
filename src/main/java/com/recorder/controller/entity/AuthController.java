package com.recorder.controller.entity;

import com.recorder.dto.AuthenticationRequest;

import com.recorder.dto.AuthenticationResponse;
import com.recorder.config.JwtService;
import com.recorder.controller.entity.Usuario;
import com.recorder.controller.entity.enuns.Roles;
import com.recorder.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        // 1. Autenticação básica (email/senha)
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getSenha()
            )
        );

        // 2. Busca o usuário com suas roles
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // 3. Mapeamento das roles para o formato Spring Security
        List<GrantedAuthority> authorities = usuario.getRoles().stream()
        	    .map((Roles role) -> new SimpleGrantedAuthority(
        	        role.getDescricao().startsWith("ROLE_")
        	            ? role.getDescricao()
        	            : "ROLE_" + role.getDescricao()
        	    ))
        	    .collect(Collectors.toList());



        // 4. Geração do token JWT
        String jwtToken = jwtService.generateToken(
            new User(
                usuario.getEmail(),
                usuario.getSenha(),
                authorities
            )
        );

        // 5. Preparação da resposta
        return ResponseEntity.ok(
            AuthenticationResponse.builder()
                .token(jwtToken)
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .roles(
                    authorities.stream()
                        .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                        .collect(Collectors.toList())
                )
                .build()
        );
    }
}