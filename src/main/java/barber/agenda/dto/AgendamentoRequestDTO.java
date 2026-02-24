package barber.agenda.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AgendamentoRequestDTO(

        @NotNull(message = "A data e hora são obrigatórias")
        @Future(message = "O agendamento deve ser para uma data futura")
        LocalDateTime dataHora,

        @NotNull(message = "O ID do barbeiro é obrigatório")
        Long barbeiroId,

        @NotNull(message = "O ID do cliente é obrigatório")
        Long clienteId
) {}