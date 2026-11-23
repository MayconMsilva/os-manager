package com.project.os_manager.ordemservico.dto;

import com.project.os_manager.ordemservico.OrdemServicoHistorico;
import com.project.os_manager.ordemservico.enums.StatusOrdemServico;

import java.time.OffsetDateTime;

public record HistoricoStatusResponse(
        Long id,
        StatusOrdemServico statusAnterior,
        StatusOrdemServico statusNovo,
        OffsetDateTime alteradoEm
) {

    public static HistoricoStatusResponse de(OrdemServicoHistorico historico){
        return new HistoricoStatusResponse(
                historico.getId(),
                historico.getStatusAnterior(),
                historico.getStatusNovo(),
                historico.getAlteradoEm()
        );
    }
}
