package com.project.os_manager.equipamento;

import com.project.os_manager.cliente.Cliente;
import com.project.os_manager.cliente.ClienteRepository;
import com.project.os_manager.cliente.dto.ClienteRequest;
import com.project.os_manager.equipamento.dto.EquipamentoRequest;
import com.project.os_manager.equipamento.dto.EquipamentoResponse;
import com.project.os_manager.exception.ConflitoException;
import com.project.os_manager.exception.RecursoNaoEncontradoException;
import com.project.os_manager.exception.RegraNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final EquipamentoRepository repository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public EquipamentoResponse criar(EquipamentoRequest request){

        Cliente cliente = buscarClienteAtivo(request.clienteId());
        String numeroSerie = normalizarNumeroSerie(request.numeroSerie());

        if (numeroSerie != null && repository.existsByNumeroSerie(numeroSerie)){
            throw new ConflitoException("Já existe um equipamento com o número de série " + numeroSerie);
        }

        Equipamento equipamento = new Equipamento();
        equipamento.setCliente(cliente);
        preencher(equipamento, request, numeroSerie);
        return EquipamentoResponse.de(repository.save(equipamento));
    }

    @Transactional
    public List<EquipamentoResponse> listar(Long clienteId){
        List<Equipamento> equipamentos = (clienteId == null)
                ? repository.findAll()
                : repository.findByClienteId(clienteId);

        return equipamentos.stream().map(EquipamentoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public EquipamentoResponse buscar(Long id){
        return EquipamentoResponse.de(buscarEntidade(id));
    }

    @Transactional
    public EquipamentoResponse atualizar(Long id, EquipamentoRequest request){
        Equipamento equipamento = buscarEntidade(id);

        if (!equipamento.getCliente().getId().equals(request.clienteId())){
            equipamento.setCliente(buscarClienteAtivo(request.clienteId()));
        }

        String numeroSerie = normalizarNumeroSerie(request.numeroSerie());
        if (numeroSerie != null && repository.existsByNumeroSerieAndIdNot(numeroSerie, id)){
            throw new ConflitoException("Já existe um equipamento com o número de série " + numeroSerie);
        }

        preencher(equipamento, request, numeroSerie);
        return EquipamentoResponse.de(equipamento);
    }

    @Transactional
    public void desativar(Long id){
        Equipamento equipamento = buscarEntidade(id);
        equipamento.setAtivo(false);
    }

    private Equipamento buscarEntidade(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Equipamento não encontrado: " + id));
    }

    private Cliente buscarClienteAtivo(Long clienteId){
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente Não Encontrado " + clienteId));

        if (!cliente.isAtivo()){
            throw new RegraNegocioException("O Cliente " + clienteId + " está inativo");
        }
        return cliente;
    }

    private void preencher(Equipamento equipamento, EquipamentoRequest request, String numeroSerie){
        equipamento.setNome(request.nome().trim());
        equipamento.setTipo(vazioParaNulo(request.tipo()));
        equipamento.setMarca(vazioParaNulo(request.marca()));
        equipamento.setModelo(vazioParaNulo(request.modelo()));
        equipamento.setNumeroSerie(numeroSerie);
        equipamento.setLocalizacao(vazioParaNulo(request.localizacao()));
    }

    private String normalizarNumeroSerie(String numeroSerie){
        String valor = vazioParaNulo(numeroSerie);
        return valor == null ? null : valor .toUpperCase();
    }

    private String vazioParaNulo(String valor){
        return (valor == null || valor.isBlank()) ? null : valor.trim();
    }
}
