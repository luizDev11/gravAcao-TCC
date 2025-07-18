package com.recorder.service;

import com.recorder.controller.entity.Usuario;
import com.recorder.controller.entity.enuns.Roles;
import com.recorder.dto.UsuarioDTO;
import com.recorder.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // ========== REGISTRO ==========
    @Transactional
    public Usuario registrar(UsuarioDTO request) {
        // Validações básicas
        if (!request.getSenha().equals(request.getConfirmarSenha())) {
            throw new RuntimeException("Senha e confirmação de senha não coincidem.");
        }

        if (!request.agreeTerms()) {
            throw new RuntimeException("Você deve aceitar os termos.");
        }

        // Verificar unicidade de email, CPF e CNPJ no banco de dados
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            logger.warn("Tentativa de registro com email já em uso: {}", request.getEmail());
            throw new RuntimeException("Email já cadastrado.");
        }

        if (request.getCpf() != null && !request.getCpf().trim().isEmpty() &&
                usuarioRepository.existsByCpf(request.getCpf())) {
            logger.warn("Tentativa de registro com CPF já em uso: {}", request.getCpf());
            throw new RuntimeException("CPF já cadastrado.");
        }

        if (request.getCnpj() != null && !request.getCnpj().trim().isEmpty() &&
                usuarioRepository.existsByCnpj(request.getCnpj())) {
            logger.warn("Tentativa de registro com CNPJ já em uso: {}", request.getCnpj());
            throw new RuntimeException("CNPJ já cadastrado.");
        }

        // Criação do usuário
        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .telefone(request.getTelefone())
                .cpf(request.getCpf())
                .cnpj(request.getCnpj() != null && !request.getCnpj().trim().isEmpty() ? request.getCnpj() : null)
                .senha(passwordEncoder.encode(request.getSenha()))
                .agendamentos(Collections.emptyList())
                .build();

        // Atribuição de roles
        Set<Roles> assignedRoles = new HashSet<>();
        if (request.getCnpj() != null && !request.getCnpj().trim().isEmpty()) {
            assignedRoles.add(Roles.ROLE_PROFISSIONAL);
            logger.info("Usuário {} registrado como PROFISSIONAL (CNPJ: {})", request.getEmail(), request.getCnpj());
        } else {
            assignedRoles.add(Roles.ROLE_USUARIO);
            logger.info("Usuário {} registrado como USUARIO (CPF: {})", request.getEmail(), request.getCpf());
        }
        usuario.setRoles(assignedRoles);

        return usuarioRepository.save(usuario);
    }

    // ========== AUTENTICAÇÃO ==========
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        return usuario;
    }

    // ========== CRUD ==========
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario atualizar(Long id, UsuarioDTO dto) {
        Usuario usuarioExistente = buscarPorId(id);

        // Atualiza campos básicos
        usuarioExistente.setNome(dto.getNome());
        usuarioExistente.setEmail(dto.getEmail());
        usuarioExistente.setTelefone(dto.getTelefone());

        // Atualiza senha se fornecida
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        // Atualiza CPF/CNPJ e roles se necessário
        if (dto.getCpf() != null && !dto.getCpf().equals(usuarioExistente.getCpf())) {
            if (usuarioRepository.existsByCpf(dto.getCpf())) {
                throw new RuntimeException("CPF já cadastrado por outro usuário");
            }
            usuarioExistente.setCpf(dto.getCpf());
        }

        if (dto.getCnpj() != null && !dto.getCnpj().equals(usuarioExistente.getCnpj())) {
            if (usuarioRepository.existsByCnpj(dto.getCnpj())) {
                throw new RuntimeException("CNPJ já cadastrado por outro usuário");
            }
            usuarioExistente.setCnpj(dto.getCnpj());

            // Atualiza roles se CNPJ foi adicionado
            Set<Roles> roles = usuarioExistente.getRoles();
            roles.add(Roles.ROLE_PROFISSIONAL);
            usuarioExistente.setRoles(roles);
        }

        return usuarioRepository.save(usuarioExistente);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }
}