package com.project.os_manager.planopreventivo;

import com.project.os_manager.equipamento.Equipamento;
import com.project.os_manager.equipamento.EquipamentoRepository;
import com.project.os_manager.exception.RecursoNaoEncontradoException;
import com.project.os_manager.exception.RegraNegocioException;
import com.project.os_manager.planopreventivo.dto.PlanoPreventivoAtualizacaoRequest;
import com.project.os_manager.planopreventivo.dto.PlanoPreventivoCriacaoRequest;
import com.project.os_manager.planopreventivo.dto.PlanoPreventivoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanoPreventivoService {

    private final PlanoPreventivoRepository repository;
    private final EquipamentoRepository equipamentoRepository;
    private final Clock clock;

    @Transactional
    public PlanoPreventivoResponse criar(PlanoPreventivoCriacaoRequest request){
        Equipamento equipamento = buscarEquipamentoElegivel(request.equipamentoId());

        PlanoPreventivo plano = new PlanoPreventivo();
        plano.setEquipamento(equipamento);
        plano.setTitulo(request.titulo().trim());
        plano.setDescricao(vazioParaNulo(request.descricao()));
        plano.setPeriodicidadeDias(request.periodicidadeDias());
        plano.setProximaExecucao(request.proximaExcecucao() != null ? request.proximaExcecucao()
                : LocalDate.now(clock).plusDays(request.periodicidadeDias()));

        if (request.prioridade() != null){
            plano.setPrioridade(request.prioridade());
        }

        return PlanoPreventivoResponse.de(repository.save(plano));
    }

    @Transactional(readOnly = true)
    public List<PlanoPreventivoResponse> listar(Long equipamentoId){
        return repository.filtrar(equipamentoId).stream()
                .map(PlanoPreventivoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlanoPreventivoResponse buscar(Long id){
        return PlanoPreventivoResponse.de(buscarEntidade(id));
    }

    @Transactional
    public PlanoPreventivoResponse atualizar(Long id, PlanoPreventivoAtualizacaoRequest request){
        PlanoPreventivo plano = buscarEntidade(id);

        if (!plano.isAtivo()){
            throw new RegraNegocioException("Não é Possível Alterar um Plano Preventivo Inativo");
        }

        plano.setTitulo(request.titulo().trim());
        plano.setDescricao(vazioParaNulo(request.descricao()));
        plano.setPeriodicidadeDias(request.periodicidadeDias());
        plano.setProximaExecucao(request.proximaExecucao());
        plano.setPrioridade(request.prioridade());

        return PlanoPreventivoResponse.de(plano);
    }

    @Transactional
    public void desativar(Long id){
        PlanoPreventivo plano = buscarEntidade(id);
        plano.setAtivo(false);
    }

    private PlanoPreventivo buscarEntidade(Long id){
        return repository.findComEquipamentoById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Plano Preventivo não encontrado" + id
                ));
    }

    private Equipamento buscarEquipamentoElegivel(Long equipamentoId){
        Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Equipamento não encontrado: " + equipamentoId
                ));

        if (!equipamento.isAtivo()){
            throw new RegraNegocioException("O Equipamento " + equipamentoId + " está Inativo");
        }
        if (!equipamento.getCliente().isAtivo()){
            throw new RegraNegocioException(
                    "O Cliente do Equipamento " + equipamentoId + " está Inativo"
            );
        }

        return equipamento;
    }

    private String vazioParaNulo(String valor){
        return (valor == null || valor.isBlank()) ? null : valor.trim();
    }
}
