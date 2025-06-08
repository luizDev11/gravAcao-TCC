package com.recorder.dto;

public class AgendamentoDTO {

    private String nomeCliente;
    private String email;
    private String telefone;
    private String plano;
    private String endereco;
    private String data;
    private String horario;
    private String status;

    public AgendamentoDTO() {
    }

    public AgendamentoDTO(String nomeCliente, String email, String telefone, String plano,
                          String endereco, String data, String horario, String status) {
        this.nomeCliente = nomeCliente;
        this.email = email;
        this.telefone = telefone;
        this.plano = plano;
        this.endereco = endereco;
        this.data = data;
        this.horario = horario;
        this.status = status;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
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

    public String getPlano() {
        return plano;
    }

    public void setPlano(String plano) {
        this.plano = plano;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


