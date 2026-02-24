package barber.agenda.service;

import barber.agenda.entity.Agendamento;
import barber.agenda.entity.enums.StatusAgendamento;
import barber.agenda.exception.BusinessException;
import barber.agenda.exception.CampoObrigatorioException;
import barber.agenda.repository.AgendamentoRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Transactional // Boa prática: garante a integridade da operação no banco
    public Agendamento agendar(Agendamento novoAgendamento) {

        // 1º: Validações de existência (Garante que o código não quebre por falta de dados)
        if (novoAgendamento.getBarbeiro() == null || novoAgendamento.getBarbeiro().getId() == null) {
            throw new BusinessException("É necessário informar um barbeiro válido.");
        }

        if (novoAgendamento.getCliente() == null || novoAgendamento.getCliente().getId() == null) {
            throw new CampoObrigatorioException("um cliente válido.");
        }

        // 2º: Validações de regras de negócio simples (Data e Status)
        if (novoAgendamento.getDataHora().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Não é possível agendar para uma data passada.");
        }

        if (novoAgendamento.getStatus() == StatusAgendamento.INDISPONIVEL) {
            throw new BusinessException("Não é possível agendar: Horário Indisponível.");
        }

        // 3º: Validação de conflito no Banco (Precisa que o ID do barbeiro exista!)
        boolean jaOcupado = agendamentoRepository.existsByBarbeiroIdAndDataHora(
                novoAgendamento.getBarbeiro().getId(),
                novoAgendamento.getDataHora()
        );

        if (jaOcupado) {
            throw new BusinessException("O barbeiro já possui um agendamento neste horário!");
        }

        // 4º: Finalização (Caminho feliz)
        return agendamentoRepository.save(novoAgendamento);
    }
    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }
}