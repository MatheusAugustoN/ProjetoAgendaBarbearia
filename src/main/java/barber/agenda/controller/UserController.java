package barber.agenda.controller;

import barber.agenda.entity.User;
import barber.agenda.repository.UserRepository;
import barber.agenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody User user) {
        // 1. Verifica se o usuário já existe para evitar erro
        if (repository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Erro: Este nome de usuário já está em uso!");
        }

        // 2. CRIPTOGRAFA a senha antes de salvar (Obrigatório!)
        user.setPassword(encoder.encode(user.getPassword()));

        // 3. Define a permissão padrão de cliente
        user.getRoles().add("ROLE_USER");

        repository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso!");
    }
}
