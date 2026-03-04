package barber.agenda.controller;

import barber.agenda.entity.User;
import barber.agenda.repository.UserRepository;
import barber.agenda.security.JWTService;
import barber.agenda.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.util.Optional; // Importante para o repository

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Use esta agora
    private UserRepository repository;

    @MockitoBean // E esta aqui também
    private PasswordEncoder encoder;

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private JWTService jwtService; // Isso resolve o erro do 'JWTFilter'

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    void cadastrar_sucesso() throws Exception {
        // Arrange
        // Como não há construtor, o Hibernate/Lombok usa o padrão
        when(repository.findByUsername("Matheus")).thenReturn(null);
        when(encoder.encode("123")).thenReturn("senhaCriptografada");

        // Act + Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "username": "Matheus",
                                "password": "123"
                            }
                        """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("Usuário criado com sucesso!"));

        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve retornar erro se usuário já existir")
    void cadastrar_usuarioExistente() throws Exception {
        // Arrange
        User userExistente = new User();
        userExistente.setUsername("Matheus");
        userExistente.setPassword("123");

        when(repository.findByUsername("Matheus")).thenReturn(userExistente);

        // Act + Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "Matheus",
                            "password": "123"
                        }
                    """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erro: Este nome de usuário já está em uso!"));
    }
}