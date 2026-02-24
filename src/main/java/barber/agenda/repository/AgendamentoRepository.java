package barber.agenda.repository;

import barber.agenda.entity.Agendamento;
import barber.agenda.entity.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    // Procura se existe agendamento para o barbeiro X na data Y
    boolean existsByBarbeiroAndDataHora(Barbeiro barbeiro, LocalDateTime dataHora);
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
}