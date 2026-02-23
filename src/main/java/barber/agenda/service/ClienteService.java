package barber.agenda.service;

import barber.agenda.entity.Cliente;
import barber.agenda.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            throw new RuntimeException("Já existe um cliente cadastrado com este CPF.");
        }

        // Regra: Validar campos obrigatórios
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            throw new RuntimeException("O nome do cliente é obrigatório.");
        }

        return repository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }
}
