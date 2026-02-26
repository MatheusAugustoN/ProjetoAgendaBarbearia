package barber.agenda.controller;

import barber.agenda.dto.ClienteRequestDTO;
import barber.agenda.dto.ClienteResponseDTO;
import barber.agenda.entity.Cliente;
import barber.agenda.service.ClienteService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject; // Para o Swagger
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; // Import correto
import org.springframework.data.domain.Pageable; // Import correto
import org.springframework.data.web.PageableDefault; // Para valores padrão
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    // Ajustei o retorno do Post para ClienteResponseDTO (melhor prática)
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> cadastrar(@RequestBody @Valid ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setTelefone(dto.telefone());

        Cliente clienteSalvo = service.cadastrar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ClienteResponseDTO(clienteSalvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new ClienteResponseDTO(service.buscarPorId(id)));
    }

    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> listarTodos(
            @ParameterObject @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        return ResponseEntity.ok(service.listarTodos(pageable));
    }
}