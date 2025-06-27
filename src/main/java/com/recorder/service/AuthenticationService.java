package com.recorder.service;

import com.recorder.controller.entity.Usuario;
import com.recorder.repository.UsuarioRepository;
import com.recorder.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Tentativa de acesso sem autenticação");
            throw new RuntimeException("Usuário não está autenticado");
        }

        String email = authentication.getName();
        logger.info("Obtendo usuário autenticado: {}", email);

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Usuário não encontrado para email: {}", email);
                    return new RuntimeException("Usuário não encontrado");
                });
    }
}