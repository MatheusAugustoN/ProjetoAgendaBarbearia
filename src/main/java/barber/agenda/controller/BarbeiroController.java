package barber.agenda.controller;

import barber.agenda.dto.BarbeiroRequestDTO;
import barber.agenda.dto.BarbeiroResponseDTO;
import barber.agenda.entity.Barbeiro;
import barber.agenda.service.BarbeiroService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject; // Importante para o Swagger
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; // IMPORT CORRETO
import org.springframework.data.domain.Pageable; // IMPORT CORRETO
import org.springframework.data.web.PageableDefault; // Para definir valores padrão
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/barbeiros")
public class BarbeiroController {

    @Autowired
    private BarbeiroService service;

    @PostMapping
    public ResponseEntity<BarbeiroResponseDTO> cadastrar(@RequestBody @Valid BarbeiroRequestDTO dto) {
        Barbeiro barbeiro = new Barbeiro();
        barbeiro.setNome(dto.nome());
        Barbeiro barbeiroSalvo = service.cadastrar(barbeiro);
        return ResponseEntity.status(HttpStatus.CREATED).body(new BarbeiroResponseDTO(barbeiroSalvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> buscarPorId(@PathVariable Long id) {
        Barbeiro barbeiro = service.buscarPorId(id);
        return ResponseEntity.ok(new BarbeiroResponseDTO(barbeiro));
    }

    // MUDANÇA AQUI: Agora retorna Page e recebe Pageable
    @GetMapping
    public ResponseEntity<Page<BarbeiroResponseDTO>> listarTodos(
            @ParameterObject @PageableDefault(size = 10, sort = "nome") Pageable pageable) {


        Page<BarbeiroResponseDTO> dtos = service.listarTodos(pageable);

        return ResponseEntity.ok(dtos);
    }
}