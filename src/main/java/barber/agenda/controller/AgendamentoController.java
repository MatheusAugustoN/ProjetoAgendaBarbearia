package barber.agenda.controller;

import barber.agenda.dto.AgendamentoRequestDTO;
import barber.agenda.dto.AgendamentoResponseDTO;
import barber.agenda.entity.Agendamento;
import barber.agenda.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<AgendamentoResponseDTO>> listarTodos(
            @PageableDefault(size = 10, sort = "dataHora", direction = Sort.Direction.ASC) Pageable pageable) {

        // O service agora retorna Page<AgendamentoResponseDTO> já convertido
        Page<AgendamentoResponseDTO> pagina = service.listarTodos(pageable);

        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        Agendamento agendamento = service.buscarPorId(id);
        return ResponseEntity.ok(new AgendamentoResponseDTO(agendamento));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Page<AgendamentoResponseDTO>> cancelar(
            @PathVariable Long id,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(service.cancelarAgendamentoPorId(id, pageable));
    }

    @GetMapping("/data")
    public ResponseEntity<Page<AgendamentoResponseDTO>> buscarPorData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @PageableDefault(size = 10, sort = "dataHora") Pageable pageable) {
        return ResponseEntity.ok(service.listarPorData(data, pageable));
    }
}
