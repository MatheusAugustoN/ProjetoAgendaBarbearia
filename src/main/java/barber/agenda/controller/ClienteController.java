package barber.agenda.controller;

import barber.agenda.dto.ClienteRequestDTO;
import barber.agenda.dto.ClienteResponseDTO;
import barber.agenda.entity.Cliente;
import barber.agenda.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody @Valid ClienteRequestDTO dto) {
        // Aqui você vai converter o DTO para Entity antes de mandar para o Service
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setTelefone(dto.telefone());

        return ResponseEntity.ok(service.cadastrar(cliente));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        // 1. Busca a Entity no banco (via Service)
        Cliente cliente = service.buscarPorId(id);

        // 2. Transforma a Entity em ResponseDTO usando o construtor que criamos
        ClienteResponseDTO response = new ClienteResponseDTO(cliente);

        // 3. Retorna o DTO
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<Cliente> clientes = service.listarTodos();

        // Converte a lista de Entities para uma lista de DTOs
        List<ClienteResponseDTO> dtos = clientes.stream()
                .map(ClienteResponseDTO::new) // Usa o construtor: public ClienteResponseDTO(Cliente cliente)
                .toList();

        return ResponseEntity.ok(dtos);
    }
}