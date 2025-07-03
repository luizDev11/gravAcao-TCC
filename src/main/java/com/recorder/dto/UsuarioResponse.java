package com.recorder.dto;

import java.util.List;
import java.util.stream.Collectors;

import java.util.Collections;

public class UsuarioResponse {
    private Long idUsuario;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private List<String> roles; // Sempre com ROLE_

    public UsuarioResponse(Long idUsuario, String nome, String email, 
                         String telefone, String cpf, List<String> roles) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.roles = roles.stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .collect(Collectors.toList());
    }

    
    public List<String> getRoles() {
        return Collections.unmodifiableList(roles);
    }
    public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	    
}