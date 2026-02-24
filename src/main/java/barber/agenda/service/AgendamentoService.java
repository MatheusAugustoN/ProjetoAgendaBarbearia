package barber.agenda.service;

import barber.agenda.dto.AgendamentoRequestDTO;
import barber.agenda.entity.Agendamento;
import barber.agenda.entity.Barbeiro;
import barber.agenda.entity.Cliente;
import barber.agenda.entity.enums.StatusAgendamento;
import barber.agenda.exception.BusinessException;
import barber.agenda.exception.CampoObrigatorioException;
import barber.agenda.repository.AgendamentoRepository;

import barber.agenda.repository.BarbeiroRepository;
import barber.agenda.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;
    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private ClienteRepository clienteRepository;


    public Agendamento agendar(AgendamentoRequestDTO dto) {

        // 1. Buscar o Barbeiro pelo ID do DTO
        Barbeiro barbeiro = barbeiroRepository.findById(dto.barbeiroId())
                .orElseThrow(() -> new BusinessException("Barbeiro não encontrado com o ID: " + dto.barbeiroId()));

        // 2. Buscar o Cliente pelo ID do DTO
        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new BusinessException("Cliente não encontrado com o ID: " + dto.clienteId()));

        // Validar se o barbeiro já tem agenda nesse horário
        if (agendamentoRepository.existsByBarbeiroAndDataHora(barbeiro, dto.dataHora())) {
            throw new BusinessException("Este barbeiro já possui um agendamento neste horário.");
        }

        // 3. Criar a Entity e preencher os dados
        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(dto.dataHora());
        agendamento.setBarbeiro(barbeiro);
        agendamento.setCliente(cliente);
        agendamento.setStatus(StatusAgendamento.INDISPONIVEL);


        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Agendamento não encontrado com o ID: " + id));
    }

    public List<Agendamento> cancelarAgendamentoPorId(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Agendamento não encontrado."));
        agendamentoRepository.deleteById(id);
        return listarTodos();
    }
    public List<Agendamento> listarPorData(LocalDate data) {
        // Define o início do dia (00:00:00) e o fim do dia (23:59:59)
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(LocalTime.MAX);

        return agendamentoRepository.findByDataHoraBetween(inicio, fim);
    }
}
