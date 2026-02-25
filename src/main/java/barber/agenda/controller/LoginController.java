package barber.agenda.controller;

import barber.agenda.dto.LoginRequestDTO;
import barber.agenda.dto.LoginResponseDTO;
import barber.agenda.entity.User;
import barber.agenda.repository.UserRepository;
import barber.agenda.security.JWTService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @PostMapping
    public ResponseEntity<LoginResponseDTO> logar(@RequestBody @Valid LoginRequestDTO loginData) {
        // 1. Busca o usuário
        User user = userRepository.findByUsername(loginData.username());

        // 2. Verifica se existe
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 3. Compara senhas
        boolean senhaValida = passwordEncoder.matches(loginData.password(), user.getPassword());

        if (!senhaValida) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 4. AJUSTADO: Chamando o método correto com Claims extras para as Roles
        // Certifique-se de que o método no JWTService aceite esses dois parâmetros
        String token = jwtService.generateToken(user.getUsername(), user.getRoles());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
