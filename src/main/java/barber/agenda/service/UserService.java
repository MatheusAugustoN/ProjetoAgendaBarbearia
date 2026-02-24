package barber.agenda.service;



import barber.agenda.entity.User;
import barber.agenda.exception.CampoObrigatorioException;
import barber.agenda.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder encoder;


    public void createUser(User user) {
        // 1. Criptografa a senha
        if (repository.findByUsername(user.getUsername()) != null) {
            throw new CampoObrigatorioException("username");
        }

        user.setPassword(encoder.encode(user.getPassword()));

        // 2. Define a Role padrão se a lista estiver vazia
        if (user.getRoles().isEmpty()) {
            user.getRoles().add("ROLE_USER");
        }

        repository.save(user);
    }

    // Método para ajudar nos testes: cria um admin se não existir usuários
    @PostConstruct
    public void initAdmin() {
        if (repository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin123"));
            admin.getRoles().add("ROLE_ADMIN");
            repository.save(admin);
        }
    }

}
