package barber.agenda.dto;


import barber.agenda.entity.Cliente;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String cpf,
        String telefone
) {
    // Construtor compacto para converter Entity -> DTO
    public ClienteResponseDTO(Cliente cliente) {
        this(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getTelefone()
        );
    }
}