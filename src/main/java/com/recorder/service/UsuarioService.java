package com.recorder.service;

import com.recorder.controller.entity.Usuario;
import com.recorder.dto.UsuarioDTO;
import com.recorder.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;



    public Usuario registrar(UsuarioDTO dto) {
        if (!dto.getSenha().equals(dto.getConfirmarSenha())) {
            throw new RuntimeException("As senhas não coincidem.");
        }

        if (!dto.isTermosAceitos()) {
            throw new RuntimeException("Você deve aceitar os termos.");
        }

        if (repository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCpf(dto.getCpf());
        usuario.setTelefone(dto.getTelefone());
        usuario.setSenha(dto.getSenha()); // criptografar em produção!

        return repository.save(usuario);
    }
}