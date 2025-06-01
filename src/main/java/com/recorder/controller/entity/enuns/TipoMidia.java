package com.recorder.controller.entity.enuns;

public enum TipoMidia {
    FOTO("Foto"),
    VIDEO("Video");

    private final String descricao;

    TipoMidia (String descricao) {
        this.descricao = descricao;
    }

}
