package barber.agenda.dto;

import barber.agenda.entity.Barbeiro;

public record BarbeiroResponseDTO(
        Long id,
        String nome
) {
    public BarbeiroResponseDTO(Barbeiro barbeiro) {
        this(
                barbeiro.getId(),
                barbeiro.getNome()
        );
    }
}
