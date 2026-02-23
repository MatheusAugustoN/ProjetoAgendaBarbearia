package barber.agenda.repository;

import barber.agenda.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    // Procura se existe agendamento para o barbeiro X na data Y
    boolean existsByBarbeiroIdAndDataHora(Long barbeiroId, LocalDateTime dataHora);
}