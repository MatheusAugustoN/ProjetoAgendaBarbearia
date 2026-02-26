package barber.agenda.repository;

import barber.agenda.entity.Barbeiro;
import org.springframework.data.domain.Page; // IMPORTANTE
import org.springframework.data.domain.Pageable; // IMPORTANTE
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {

    boolean existsByNome(String nome);

    // O JpaRepository já tem o findAll(Pageable),
    // mas se você for criar métodos personalizados (como busca por nome),
    // eles devem seguir este padrão:
    Page<Barbeiro> findByNomeContaining(String nome, Pageable pageable);
}