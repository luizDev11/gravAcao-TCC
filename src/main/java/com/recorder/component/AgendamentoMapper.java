package com.recorder.component;

import com.recorder.controller.entity.Agendamento;
import com.recorder.controller.entity.Usuario;
import com.recorder.controller.entity.enuns.StatusAgendamento;
import com.recorder.dto.AgendamentoDTO;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoMapper {

    public Agendamento toEntity(AgendamentoDTO dto, Usuario usuario) {
        Agendamento agendamento = new Agendamento();
        AgendamentoDTO agendamentoDTO = new AgendamentoDTO();

        agendamento.setUsuario(usuario);
        agendamento.setNome(agendamentoDTO.getNome());
        agendamento.setEmail(agendamentoDTO.getEmail());
        agendamento.setTelefone(agendamentoDTO.getTelefone());
        agendamento.setPlano(agendamentoDTO.getPlano());
        agendamento.setData(agendamentoDTO.getData());
        agendamento.setHorario(agendamentoDTO.getHorario());
        agendamento.setEsporte(agendamentoDTO.getEsporte());
        agendamento.setLocal(agendamentoDTO.getLocal());
        agendamento.setLatitude(agendamentoDTO.getLatitude());
        agendamento.setLongitude(agendamentoDTO.getLongitude());
        agendamento.setStatus(StatusAgendamento.PENDENTE);
        return agendamento;
    }
}
