package com.recorder.service;

import com.recorder.controller.entity.Agendamento;
import com.recorder.controller.entity.Usuario;
import com.recorder.controller.entity.enuns.StatusAgendamento;
import com.recorder.dto.AgendamentoDTO;
import com.recorder.repository.AgendamentoRepository;
import com.recorder.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Agendamento criarAgendamento(AgendamentoDTO dto, String emailUsuario) {
        // Busca o usuário pelo e-mail do token
       

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + emailUsuario));

        Agendamento agendamento = new Agendamento();
        agendamento.setUsuario(usuario);

        agendamento.setNome(dto.getNome());
        agendamento.setEmail(dto.getEmail());
        agendamento.setTelefone(dto.getTelefone());
        agendamento.setPlano(dto.getPlano());
        agendamento.setData(dto.getData());
        agendamento.setHorario(dto.getHorario());
        agendamento.setEsporte(dto.getEsporte());
        agendamento.setLocal(dto.getLocal());
        agendamento.setLatitude(dto.getLatitude());
        agendamento.setLongitude(dto.getLongitude());


        agendamento.setStatus(dto.getStatus() != null ? dto.getStatus() : StatusAgendamento.PENDENTE);

        return agendamentoRepository.save(agendamento);
    }
}

