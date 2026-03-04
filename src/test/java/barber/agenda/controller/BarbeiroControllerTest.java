package barber.agenda.controller;



import barber.agenda.dto.BarbeiroResponseDTO;
import barber.agenda.entity.Barbeiro;
import barber.agenda.entity.User;
import barber.agenda.exception.BusinessException;
import barber.agenda.repository.BarbeiroRepository;
import barber.agenda.repository.UserRepository;
import barber.agenda.security.JWTService;

import barber.agenda.service.BarbeiroService;
import barber.agenda.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BarbeiroController.class)
@AutoConfigureMockMvc(addFilters = false)
 class BarbeiroControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BarbeiroRepository repository;
    @MockitoBean
    private JWTService jwtService;
    @MockitoBean
    private BarbeiroService service;
    @MockitoBean
    private Barbeiro barbeiro;


    @Test
    @DisplayName("Deve cadastrar Barbeiro com sucesso")
    void cadastrarBarbeiro() throws Exception {
        // 1. Arrange (Preparação)
        Barbeiro barbeiroSalvo = new Barbeiro();
        barbeiroSalvo.setId(1L);
        barbeiroSalvo.setNome("Matheus");

        // Precisamos mockar o service, já que a Controller o chama
        // Nota: Use @MockitoBean se estiver no Spring Boot 3.4+
        when(service.cadastrar(any(Barbeiro.class))).thenReturn(barbeiroSalvo);

        // 2. Act + Assert
        mockMvc.perform(post("/barbeiros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "nome": "Matheus"
                        }
                    """))
                .andExpect(status().isCreated())
                // Em vez de content().string(), verificamos o campo do JSON:
                .andExpect(jsonPath("$.nome").value("Matheus"))
                .andExpect(jsonPath("$.id").value(1));

        // 3. Verify
        verify(service).cadastrar(any(Barbeiro.class));
    }

    @Test
    @DisplayName("Deve retornar erro quando barbeiro já existe")
    void erroBarbeiroExiste() throws Exception {
        // Aqui você configura o Mock para LANÇAR uma exceção em vez de devolver um objeto
        when(service.cadastrar(any(Barbeiro.class)))
                .thenThrow(new BusinessException("Este barbeiro já está cadastrado."));

        mockMvc.perform(post("/barbeiros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Matheus\"}"))
                .andExpect(status().isBadRequest()); // Espera um erro 400
    }
}
