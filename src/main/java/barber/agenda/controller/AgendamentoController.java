package barber.agenda.controller;

import barber.agenda.dto.AgendamentoRequestDTO;
import barber.agenda.dto.AgendamentoResponseDTO;
import barber.agenda.entity.Agendamento;
import barber.agenda.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> agendar(@RequestBody @Valid AgendamentoRequestDTO dto) {
        // O Controller não cria a Entity nem busca IDs. Ele delega tudo ao Service.
        Agendamento agendamentoSalvo = service.agendar(dto);

        // Retornamos o ResponseDTO usando o construtor de conversão que criamos
        return ResponseEntity.status(HttpStatus.CREATED).body(new AgendamentoResponseDTO(agendamentoSalvo));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDTO>> listarTodos() {
        List<Agendamento> agendamentos = service.listarTodos();

        // Converte a lista de entidades para DTOs de resposta
        List<AgendamentoResponseDTO> dtos = agendamentos.stream()
                .map(AgendamentoResponseDTO::new)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        Agendamento agendamento = service.buscarPorId(id);
        return ResponseEntity.ok(new AgendamentoResponseDTO(agendamento));
    }

    @PostMapping("/cancelar/{id}")
    public ResponseEntity<List<AgendamentoResponseDTO>> cancelar(@PathVariable Long id) {
        List<Agendamento> lagendamento = service.cancelarAgendamentoPorId(id);
        // 2. Convertemos a lista de Entities para uma lista de ResponseDTOs
        List<AgendamentoResponseDTO> dtos = lagendamento.stream()
                .map(AgendamentoResponseDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarPorData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        List<Agendamento> agendamentos = service.listarPorData(data);

        List<AgendamentoResponseDTO> dtos = agendamentos.stream()
                .map(AgendamentoResponseDTO::new)
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
