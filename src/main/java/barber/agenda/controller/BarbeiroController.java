package barber.agenda.controller;


import barber.agenda.entity.Barbeiro;
import barber.agenda.service.BarbeiroService;
import barber.agenda.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbeiros")
public class BarbeiroController {
    @Autowired
    private BarbeiroService barbeiroService;
    @PostMapping
    public ResponseEntity<Barbeiro> cadastrar(@RequestBody Barbeiro barbeiro) {
        // 1. Chama o service usando a variável injetada (minúscula)
        Barbeiro salvo = barbeiroService.cadastrar(barbeiro);
        // 2. Retorna o envelope (ResponseEntity) com status 201 e o objeto no corpo
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
    @GetMapping // Define que este método responde a requisições GET em /barbeiros
    public ResponseEntity<List<Barbeiro>> listarTodos() {
        // 1. Busca a lista através do Service
        List<Barbeiro> lista = barbeiroService.listarTodos();
        // 2. Retorna 200 OK com a lista no corpo da resposta
        return ResponseEntity.ok(lista);
    }
}
