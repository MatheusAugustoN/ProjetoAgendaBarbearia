package barber.agenda.repository;

import barber.agenda.entity.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {
    // O Spring entende: SELECT COUNT > 0 FROM barbeiro WHERE nome = ?
    boolean existsByNome(String nome);
}
