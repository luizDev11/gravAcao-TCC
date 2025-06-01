package com.recorder.controller.entity.enuns;

public enum TipoStatusPagamento {
    CONFIRMADO("Confirmado"),
    PENDENTE("Pendente"),
    CANCELADO("Cancelado");

    private final String descricao;

    TipoStatusPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
