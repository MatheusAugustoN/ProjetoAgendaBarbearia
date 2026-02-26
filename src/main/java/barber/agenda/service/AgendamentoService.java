package barber.agenda.service;

import barber.agenda.dto.AgendamentoRequestDTO;
import barber.agenda.dto.AgendamentoResponseDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    public Page<AgendamentoResponseDTO> listarTodos(Pageable pageable) {
        // 1. Busca a página de Entities no banco
        Page<Agendamento> paginaEntities = agendamentoRepository.findAll(pageable);

        // 2. Converte a página de Entities para uma página de DTOs
        // O Java usará automaticamente aquele construtor que você criou
        return paginaEntities.map(AgendamentoResponseDTO::new);
    }

    public Agendamento buscarPorId(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Agendamento não encontrado com o ID: " + id));
    }

    public Page<AgendamentoResponseDTO> cancelarAgendamentoPorId(Long id, Pageable pageable) {
        // 1. Verifica se existe antes de deletar
        if (!agendamentoRepository.existsById(id)) {
            throw new BusinessException("Agendamento não encontrado.");
        }

        agendamentoRepository.deleteById(id);

        // 2. Retorna a lista atualizada e paginada
        return listarTodos(pageable);
    }

    public Page<AgendamentoResponseDTO> listarPorData(LocalDate data, Pageable pageable) {
        // Define o intervalo do dia
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(LocalTime.MAX);

        // Busca no repositório passando o pageable e converte para DTO
        return agendamentoRepository.findByDataHoraBetween(inicio, fim, pageable)
                .map(AgendamentoResponseDTO::new);
    }
}
