package com.recorder.controller.entity;


import jakarta.persistence.Entity;


public class Endereco {

    private Long id;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String pais;

    // Construtores
    public Endereco() {
        // Construtor padrão necessário para JPA
    }

    public Endereco(String logradouro, String numero, String bairro, String cidade,
                    String estado, String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }
}
