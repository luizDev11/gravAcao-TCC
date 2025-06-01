package com.recorder.controller.entity.enuns;


public enum TipoStatusEvento {
   // AGENDADO("Agendado"),
    CONFIRMADO("Confirmado"),
    CONCLUIDO("Concluído"),
    CANCELADO("Cancelado");

    private final String descricao;

    TipoStatusEvento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
