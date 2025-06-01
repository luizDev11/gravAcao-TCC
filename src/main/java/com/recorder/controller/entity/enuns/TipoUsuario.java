package com.recorder.controller.entity.enuns;

public enum TipoUsuario {
    CLIENTE("Cliente"),
    ADMIN("Admnistrador"),
    PROFISSIONAL("Profissional");

    private final String descricao;

    TipoUsuario(String descricao) {
        this.descricao = descricao;
    }
    public String getDescricao() {
        return descricao;
    }
}
