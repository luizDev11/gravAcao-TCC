package com.recorder.controller.entity.enuns;


public enum StatusAgendamento {
    PENDENTE("Pendente", "Agendamento aguardando confirmação"),
    CONFIRMADO("Confirmado", "Agendamento confirmado e válido"),
    RECUSADO("Recusado", "Agendamento recusado ou cancelado");


    private final String descricao;
    private final String detalhes;

    StatusAgendamento(String descricao, String detalhes) {
        this.descricao = descricao;
        this.detalhes = detalhes;
    }

    // Getters
    public String getDescricao() {
        return descricao;
    }

    public String getDetalhes() {
        return detalhes;
    }

    // Método para verificar se o status é positivo
    public boolean isConfirmado() {
        return this == CONFIRMADO;
    }

    // Método para verificar se o status permite alteração
    public boolean permiteAlteracao() {
        return this == PENDENTE;
    }

    // Método para converter de String (útil para forms/APIs)
    public static StatusAgendamento fromString(String text) {
        if (text != null) {
            for (StatusAgendamento status : StatusAgendamento.values()) {
                if (text.equalsIgnoreCase(status.name()) ||
                        text.equalsIgnoreCase(status.descricao)) {
                    return status;
                }
            }
        }
        throw new IllegalArgumentException("Nenhum status encontrado para: " + text);
    }

    // Método para obter todos os valores como String (útil para frontend)
    public static String[] names() {
        StatusAgendamento[] statuses = values();
        String[] names = new String[statuses.length];

        for (int i = 0; i < statuses.length; i++) {
            names[i] = statuses[i].name();
        }

        return names;
    }
}
