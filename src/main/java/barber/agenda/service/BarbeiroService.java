package barber.agenda.service;

import barber.agenda.entity.Barbeiro;
import barber.agenda.repository.BarbeiroRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarbeiroService {

    @Autowired
    private BarbeiroRepository repository;

    @Transactional
    public Barbeiro cadastrar(Barbeiro barbeiro) {
        // Regra: Garantir que o nome do barbeiro foi preenchido
        if (barbeiro.getNome() == null || barbeiro.getNome().trim().isEmpty()) {
            throw new RuntimeException("O nome do barbeiro não pode estar vazio.");
        }

        return repository.save(barbeiro);
    }

    public List<Barbeiro> listarTodos() {
        return repository.findAll();
    }

    public Barbeiro buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado."));
    }
}
