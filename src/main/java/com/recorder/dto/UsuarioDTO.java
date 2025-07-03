package com.recorder.dto;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.constraints.*;

public class UsuarioDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpf;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone inválido")
    private String telefone;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String senha;

    @NotBlank(message = "Confirmação de senha é obrigatória")
    private String confirmarSenha;

    @AssertTrue(message = "Você deve aceitar os termos")
    private boolean agreeTerms;

    // Getters e Setters mantidos iguais
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        // Remove formatação do CPF antes de armazenar
        this.cpf = cpf != null ? cpf.replaceAll("\\D", "") : null;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        // Remove formatação do telefone antes de armazenar
        this.telefone = telefone != null ? telefone.replaceAll("\\D", "") : null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : null;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    public boolean agreeTerms() {
        return agreeTerms;
    }

    public void setAgreeTerms(boolean agreeTerms) {
        this.agreeTerms = agreeTerms;
    }

    private List<String> roles;

    // Método para garantir o formato ROLE_
    public List<String> getRoles() {
        if (this.roles == null) {
            return List.of("ROLE_USUARIO"); // Valor padrão
        }
        return this.roles.stream()
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .collect(Collectors.toList());
    }

    public void setRoles(List<String> roles) {
        this.roles = roles != null ? 
                roles.stream()
                        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                        .collect(Collectors.toList()) : 
                List.of("ROLE_USUARIO");
    }

    // Métodos de validação melhorados
    public void validar() {
        if (!senhasConferem()) {
            throw new IllegalArgumentException("Senha e confirmação não coincidem!");
        }
        if (!agreeTerms) {
            throw new IllegalArgumentException("Você deve aceitar os termos!");
        }
    }

    private boolean senhasConferem() {
        return senha != null && senha.equals(confirmarSenha);
    }
}