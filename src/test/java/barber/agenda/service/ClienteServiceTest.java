package barber.agenda.service;

// JUnit 5 (Assertions e Anotações)
import static org.junit.jupiter.api.Assertions.*;

import barber.agenda.dto.ClienteResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
// Mockito
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

// Suas Classes (Verifique se o pacote da BusinessException está correto)
import barber.agenda.entity.Cliente;
import barber.agenda.repository.ClienteRepository;
import barber.agenda.exception.BusinessException;

// Java Util
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository; // Cria o dublê

    @InjectMocks
    private ClienteService service; // Injeta o dublê dentro do service real

    @Test
    @DisplayName("Deve cadastrar um cliente com sucesso")
    void deveCadastrarClienteComSucesso() {
        // 1. ARRANGE (Preparação)
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setCpf("12345678900");

        // Dizemos ao Mock: "Quando chamarem o findByCpf, responda que está vazio"
        Mockito.when(repository.findByCpf("12345678900")).thenReturn(Optional.empty());
        // E quando salvar, retorne o próprio cliente
        Mockito.when(repository.save(any(Cliente.class))).thenReturn(cliente);

        // 2. ACT (Ação)
        Cliente salvo = service.cadastrar(cliente);

        // 3. ASSERT (Verificação)
        assertNotNull(salvo);
        assertEquals("João Silva", salvo.getNome());
        // Verifica se o repository foi chamado exatamente 1 vez
        verify(repository, times(1)).save(cliente);
    }

    @Test
    @DisplayName("Deve lançar erro ao cadastrar CPF duplicado")
    void deveLancarErroCpfDuplicado() {
        // ARRANGE
        Cliente cliente = new Cliente();
        cliente.setCpf("12345678900");

        // Mock simulando que o CPF já existe no banco
        Mockito.when(repository.findByCpf("12345678900")).thenReturn(Optional.of(cliente));

        // ACT & ASSERT
        assertThrows(BusinessException.class, () -> service.cadastrar(cliente));
        verify(repository, never()).save(any()); // Garante que NÃO salvou no banco
    }

    @Test
    @DisplayName("Deve listar clientes paginados")
    void deveListarClientesPaginados() {
        // ARRANGE
        Pageable pageable = PageRequest.of(0, 10);
        Cliente cliente = new Cliente();
        cliente.setNome("Matheus");

        // Criamos uma página fake com 1 cliente
        Page<Cliente> paginaFake = new PageImpl<>(List.of(cliente));

        Mockito.when(repository.findAll(pageable)).thenReturn(paginaFake);

        // ACT
        Page<ClienteResponseDTO> resultado = service.listarTodos(pageable);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Matheus", resultado.getContent().get(0).nome());
        verify(repository, times(1)).findAll(pageable);
    }
}
