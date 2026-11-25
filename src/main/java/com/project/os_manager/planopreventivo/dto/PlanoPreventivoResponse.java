package com.project.os_manager.planopreventivo.dto;

import com.project.os_manager.ordemservico.enums.PrioridadeOrdemServico;
import com.project.os_manager.planopreventivo.PlanoPreventivo;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record PlanoPreventivoResponse(

        Long id,
        Long equipamentoId,
        String equipamentoNome,
        String titulo,
        String descricao,
        int periodicidadeDias,
        LocalDate proximaExecucao,
        PrioridadeOrdemServico prioridadeOrdemServico,
        boolean ativo,
        OffsetDateTime criadoEm
) {

    public static PlanoPreventivoResponse de(PlanoPreventivo plano){
        return new PlanoPreventivoResponse(
                plano.getId(),
                plano.getEquipamento().getId(),
                plano.getEquipamento().getNome(),
                plano.getTitulo(),
                plano.getDescricao(),
                plano.getPeriodicidadeDias(),
                plano.getProximaExecucao(),
                plano.getPrioridade(),
                plano.isAtivo(),
                plano.getCriadoEm()
        );
    }
}
