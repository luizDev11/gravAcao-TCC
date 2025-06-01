package com.recorder.dto;


import jakarta.validation.constraints.*;

public class UsuarioDTO {
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String senha;
    private String confirmarSenha;
    private boolean termosAceitos;

    // Getters e Setters
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
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean isTermosAceitos() {
        return termosAceitos;
    }

    public void setTermosAceitos(boolean termosAceitos) {
        this.termosAceitos = termosAceitos;
    }

    // Métodos de validação
    public boolean senhasConferem() {
        if (senha == null || confirmarSenha == null) {
            return false;
        }
        return senha.equals(confirmarSenha);
    }

    public void validarSenha() {
        if (!senhasConferem()) {
            throw new IllegalArgumentException("Senha e confirmação não coincidem!");
        }
    }

    // Adicione outras validações conforme necessário
    public void validarCampos() {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email é obrigatório!");
        }
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF é obrigatório!");
        }
        // Adicione mais validações para outros campos
    }
}