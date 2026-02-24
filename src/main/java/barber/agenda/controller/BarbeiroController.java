package barber.agenda.controller;


import barber.agenda.dto.BarbeiroRequestDTO;
import barber.agenda.dto.BarbeiroResponseDTO;
import barber.agenda.entity.Barbeiro;
import barber.agenda.service.BarbeiroService;
import barber.agenda.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbeiros")
public class BarbeiroController {

    @Autowired
    private BarbeiroService service;

    @PostMapping
    public ResponseEntity<BarbeiroResponseDTO> cadastrar(@RequestBody @Valid BarbeiroRequestDTO dto) {
        // 1. Converte RequestDTO -> Entity (pode fazer aqui ou no Service)
        Barbeiro barbeiro = new Barbeiro();
        barbeiro.setNome(dto.nome());


        // 2. Chama o Service
        Barbeiro barbeiroSalvo = service.cadastrar(barbeiro);

        // 3. Retorna o ResponseDTO usando o construtor inteligente que criamos
        return ResponseEntity.status(HttpStatus.CREATED).body(new BarbeiroResponseDTO(barbeiroSalvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> buscarPorId(@PathVariable Long id) {
        Barbeiro barbeiro = service.buscarPorId(id);
        return ResponseEntity.ok(new BarbeiroResponseDTO(barbeiro));
    }

    @GetMapping
    public ResponseEntity<List<BarbeiroResponseDTO>> listarTodos() {
        List<Barbeiro> barbeiros = service.listarTodos();

        // Converte a lista de entidades para lista de DTOs usando Stream
        List<BarbeiroResponseDTO> dtos = barbeiros.stream()
                .map(BarbeiroResponseDTO::new)
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
