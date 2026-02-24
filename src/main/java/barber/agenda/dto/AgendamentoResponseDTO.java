package barber.agenda.dto;

import barber.agenda.entity.Agendamento;
import barber.agenda.entity.enums.StatusAgendamento;

import java.time.LocalDateTime;

public record AgendamentoResponseDTO(
        Long id,
        LocalDateTime dataHora,
        String nomeBarbeiro,
        String nomeCliente,
        StatusAgendamento status
) {
    // Construtor para converter a Entity complexa em um DTO simples
    public AgendamentoResponseDTO(Agendamento agendamento) {
        this(
                agendamento.getId(),
                agendamento.getDataHora(),
                agendamento.getBarbeiro().getNome(),
                agendamento.getCliente().getNome(),
                agendamento.getStatus()
        );
    }
}