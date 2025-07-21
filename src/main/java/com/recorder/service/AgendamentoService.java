package com.recorder.service;

import com.recorder.controller.entity.Agendamento;
import com.recorder.controller.entity.Usuario;
import com.recorder.controller.entity.enuns.StatusAgendamento;
import com.recorder.dto.AgendamentoDTO;
import com.recorder.repository.AgendamentoRepository;
import com.recorder.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
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
        // ✨ CORREÇÃO AQUI: Garante que sempre seja PENDENTE na criação e remove a linha duplicada ✨
        agendamento.setStatus(StatusAgendamento.PENDENTE); // Atribui o ENUM PENDENTE


       // agendamento.setStatus(dto.getStatus() != null ? dto.getStatus() : StatusAgendamento.PENDENTE);

        return agendamentoRepository.save(agendamento);
    }

    // --- MÉTODOS PARA PROFISSIONAL ---

    // ✨ CORREÇÃO: O parâmetro 'status' deve ser do tipo StatusAgendamento (ENUM) ✨
    public List<Agendamento> getAgendamentosByStatus(StatusAgendamento status) {
        // ✨ CORREÇÃO: O método no repositório DEVE ser findByStatusOrderByDataAscHorarioAsc,
        //   e ele deve aceitar o ENUM. (Verifique o Repositório!) ✨
        return agendamentoRepository. findByStatusOrderByDataAscHorarioAsc(status);
    }

    @Transactional
    // ✨ CORREÇÃO: O parâmetro 'newStatus' deve ser do tipo StatusAgendamento (ENUM) ✨
    public Agendamento updateAgendamentoStatus(Long id, StatusAgendamento newStatus) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado"));

        // ✨ CORREÇÃO: Comparar com os valores do ENUM, não Strings literais ✨
        if (agendamento.getStatus().equals(StatusAgendamento.CONFIRMADO) ||
                agendamento.getStatus().equals(StatusAgendamento.RECUSADO))
                {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Agendamento já foi processado ou cancelado.");
        }

        // ✨ CORREÇÃO: Atribuir o ENUM, não uma String ✨
        agendamento.setStatus(newStatus);
        log.info("Agendamento ID {} atualizado para status: {}", id, newStatus);
        return agendamentoRepository.save(agendamento);
    }
    public Optional<Agendamento> getAgendamentoById(Long id) {
        return agendamentoRepository.findById(id); // Usa o método findById do JpaRepository
    }
}


