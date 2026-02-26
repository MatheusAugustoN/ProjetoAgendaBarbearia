package barber.agenda.service;

import barber.agenda.dto.BarbeiroResponseDTO;
import barber.agenda.entity.Barbeiro;
import barber.agenda.exception.BusinessException;
import barber.agenda.exception.CampoObrigatorioException;
import barber.agenda.repository.BarbeiroRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class BarbeiroService {

    @Autowired
    private BarbeiroRepository repository;

    @Transactional
    public Barbeiro cadastrar(Barbeiro barbeiro) {
        // Regra: Garantir que o nome do barbeiro foi preenchido
        if (barbeiro.getNome() == null || barbeiro.getNome().trim().isEmpty()) {
            throw new CampoObrigatorioException("nome");
        }
        // Se o barbeiro já existe, lançamos a BusinessException
        if (repository.existsByNome(barbeiro.getNome())) {
            throw new BusinessException("Este barbeiro já está cadastrado.");
        }

        return repository.save(barbeiro);
    }

    public Page<BarbeiroResponseDTO> listarTodos(Pageable pageable) {
        return repository.findAll(pageable)
                .map(BarbeiroResponseDTO::new);
    }

    public Barbeiro buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbeiro não encontrado."));
    }
}
