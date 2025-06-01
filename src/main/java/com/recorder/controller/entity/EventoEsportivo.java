package com.recorder.controller.entity;

import com.recorder.controller.entity.enuns.TipoStatusEvento;

import java.sql.Time;
import java.util.Date;

public class EventoEsportivo {
    private Integer idEventoEsportivo;
    private String descricaoEvento;
    private Date dataEvento;
    private Time horaInicio;
    private Time horaFim;
    private Endereco Local;
    private TipoStatusEvento tipoStatusEvento;
}
