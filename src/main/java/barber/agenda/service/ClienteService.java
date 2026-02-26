package barber.agenda.service;

import barber.agenda.dto.ClienteResponseDTO;
import barber.agenda.entity.Barbeiro;
import barber.agenda.entity.Cliente;
import barber.agenda.exception.BusinessException;
import barber.agenda.exception.CampoObrigatorioException;
import barber.agenda.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Transactional
    public Cliente cadastrar(Cliente cliente) {
        // Regra: Validar se o CPF já existe
        Optional<Cliente> clienteExistente = repository.findByCpf(cliente.getCpf());
        if (clienteExistente.isPresent()) {
            throw new BusinessException("Já existe um cliente cadastrado com este CPF.");
        }

        // Regra: Validar campos obrigatórios
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            throw new CampoObrigatorioException("nome do cliente.");
        }

        return repository.save(cliente);
    }

    public Page<ClienteResponseDTO> listarTodos(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ClienteResponseDTO::new);
    }

    public Cliente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente com ID " + id + " não encontrado."));
    }
}
