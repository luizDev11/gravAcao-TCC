package com.recorder.dto;

import com.recorder.controller.entity.Agendamento;
import com.recorder.controller.entity.enuns.StatusAgendamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "DTO para criação e atualização de agendamentos")
public class AgendamentoDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome não pode exceder 100 caracteres")
    @Schema(description = "Nome do solicitante", example = "João Silva")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Schema(description = "Email do solicitante", example = "joao@email.com")
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10,15}$", message = "Telefone inválido. Use o formato +5511999999999 ou 11999999999")
    @Schema(description = "Telefone de contato", example = "11999999999")
    private String telefone;

    @Size(max = 50, message = "Plano não pode exceder 50 caracteres")
    @Schema(description = "Tipo de plano", example = "Premium", nullable = true)
    private String plano;

    @NotNull(message = "Data é obrigatória")
    @FutureOrPresent(message = "Data deve ser atual ou futura")
    @Schema(description = "Data do agendamento", example = "2024-12-31")
    private LocalDate data;

    @NotNull(message = "Horário é obrigatório")
    @Schema(description = "Horário do agendamento", example = "14:00:00")
    private LocalTime horario;

    @NotBlank(message = "Esporte é obrigatório")
    @Size(max = 50, message = "Esporte não pode exceder 50 caracteres")
    @Schema(description = "Esporte a ser praticado", example = "Tênis")
    private String esporte;

    @NotBlank(message = "Local é obrigatório")
    @Size(max = 200, message = "Local não pode exceder 200 caracteres")
    @Schema(description = "Local do agendamento", example = "Quadra Central")
    private String local;

    @NotNull(message = "Latitude é obrigatória")
    @Digits(integer = 3, fraction = 6, message = "Latitude deve ter no máximo 3 dígitos inteiros e 6 decimais")
    @Schema(description = "Coordenada de latitude", example = "-23.550520")
    private BigDecimal latitude;

    @NotNull(message = "Longitude é obrigatória")
    @Digits(integer = 3, fraction = 6, message = "Longitude deve ter no máximo 3 dígitos inteiros e 6 decimais")
    @Schema(description = "Coordenada de longitude", example = "-46.633308")
    private BigDecimal longitude;

    @Schema(description = "Status do agendamento", example = "PENDENTE", allowableValues = {"PENDENTE", "CONFIRMADO", "CANCELADO"})
    private StatusAgendamento status = StatusAgendamento.PENDENTE;

    // Construtor padrão
    public AgendamentoDTO() {}

    // Getters
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getPlano() {
        return plano;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public String getEsporte() {
        return esporte;
    }

    public String getLocal() {
        return local;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setPlano(String plano) {
        this.plano = plano;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public void setEsporte(String esporte) {
        this.esporte = esporte;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    // Método para conversão de Entidade para DTO
    public static AgendamentoDTO fromEntity(Agendamento agendamento) {
        if (agendamento == null) {
            return null;
        }

        AgendamentoDTO dto = new AgendamentoDTO();
        dto.setNome(agendamento.getNome());
        dto.setEmail(agendamento.getEmail());
        dto.setTelefone(agendamento.getTelefone());
        dto.setPlano(agendamento.getPlano());
        dto.setData(agendamento.getData());
        dto.setHorario(agendamento.getHorario());
        dto.setEsporte(agendamento.getEsporte());
        dto.setLocal(agendamento.getLocal());
        dto.setLatitude(agendamento.getLatitude());
        dto.setLongitude(agendamento.getLongitude());
        dto.setStatus(agendamento.getStatus());

        return dto;
    }

    // Método para atualização de entidade
    public void updateEntity(Agendamento agendamento) {
        if (agendamento == null) {
            throw new IllegalArgumentException("Agendamento não pode ser nulo");
        }

        agendamento.setNome(this.nome);
        agendamento.setEmail(this.email);
        agendamento.setTelefone(this.telefone);
        agendamento.setPlano(this.plano);
        agendamento.setData(this.data);
        agendamento.setHorario(this.horario);
        agendamento.setEsporte(this.esporte);
        agendamento.setLocal(this.local);
        agendamento.setLatitude(this.latitude);
        agendamento.setLongitude(this.longitude);

        if (this.status != null) {
            agendamento.setStatus(this.status);
        }
    }
}