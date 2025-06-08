package com.recorder.config;

import com.recorder.controller.entity.enuns.StatusAgendamento;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class StringToStatusAgendamentoConverter implements Converter<String, StatusAgendamento> {

    @Override
    public StatusAgendamento convert(String source) {
        try {
            return StatusAgendamento.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status de agendamento inválido: " + source +
                    ". Valores aceitos: " + Arrays.toString(StatusAgendamento.values()));
        }
    }
}