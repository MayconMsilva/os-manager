package com.project.os_manager.ordemservico.dto;

import com.project.os_manager.ordemservico.OrdemServicoEntity;
import com.project.os_manager.ordemservico.enums.PrioridadeOrdemServico;
import com.project.os_manager.ordemservico.enums.StatusOrdemServico;
import com.project.os_manager.ordemservico.enums.TipoOrdemServico;

import java.time.OffsetDateTime;

public record OrdemServicoResponse(

        Long id,
        Long equipamentoId,
        String equipamentoNome,
        Long clienteId,
        String clienteNome,
        String titulo,
        String descricao,
        TipoOrdemServico tipo,
        StatusOrdemServico status,
        PrioridadeOrdemServico prioridade,
        OffsetDateTime abertaEm,
        OffsetDateTime concluidaEm
) {

    public static OrdemServicoResponse de(OrdemServicoEntity os){
        return new OrdemServicoResponse(
                os.getId(),
                os.getEquipamento().getId(),
                os.getEquipamento().getNome(),
                os.getEquipamento().getCliente().getId(),
                os.getEquipamento().getCliente().getNome(),
                os.getTitulo(),
                os.getDescricao(),
                os.getTipo(),
                os.getStatus(),
                os.getPrioridade(),
                os.getAbertaEm(),
                os.getConcluidaEm()
        );
    }
}
