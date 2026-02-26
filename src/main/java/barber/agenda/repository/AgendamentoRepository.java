package barber.agenda.repository;

import barber.agenda.entity.Agendamento;
import barber.agenda.entity.Barbeiro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Este continua igual, pois retorna um booleano de verificação
    boolean existsByBarbeiroAndDataHora(Barbeiro barbeiro, LocalDateTime dataHora);

    // MUDANÇA AQUI: Adicionamos Pageable e mudamos o retorno para Page
    // Isso permitirá buscar agendamentos por período de forma paginada
    Page<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim, Pageable pageable);

    // DICA: Adicione o findAll paginado (o JpaRepository já tem, mas é bom deixar explícito se quiser customizar)
    Page<Agendamento> findAll(Pageable pageable);
}