package com.recorder.controller.entity.enuns;

public enum Roles {
    USUARIO("USUARIO"),
    ADMIN("ADMIN"),
    PROFISSIONAL("PROFISSIONAL");

    private final String descricao;

    Roles(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

