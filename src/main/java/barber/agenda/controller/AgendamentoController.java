package barber.agenda.controller;

import barber.agenda.entity.Agendamento;
import barber.agenda.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @PostMapping
    public ResponseEntity<Agendamento> criar(@RequestBody Agendamento agendamento) {
        // Aqui a mágica acontece: o JSON vira o objeto 'agendamento'
        Agendamento salvo = service.agendar(agendamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public List<Agendamento> listarTodos() {
        return service.listarTodos(); // Crie este método no seu AgendamentoService também!
    }
}
