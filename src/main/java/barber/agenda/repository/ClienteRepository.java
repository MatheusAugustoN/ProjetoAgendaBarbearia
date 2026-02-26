package barber.agenda.repository;

import barber.agenda.entity.Cliente;
import org.springframework.data.domain.Page; // Import correto
import org.springframework.data.domain.Pageable; // Import correto
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCpf(String cpf);

    // Opcional: Se quiser buscar por nome paginado no futuro
    Page<Cliente> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}