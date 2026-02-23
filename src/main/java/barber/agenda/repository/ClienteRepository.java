package barber.agenda.repository;

import barber.agenda.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // O Spring gera a lógica de busca por CPF automaticamente
    Optional<Cliente> findByCpf(String cpf);
}
