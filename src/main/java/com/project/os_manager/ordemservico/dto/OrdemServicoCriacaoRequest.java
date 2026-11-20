package com.project.os_manager.ordemservico.dto;

import com.project.os_manager.ordemservico.enums.PrioridadeOrdemServico;
import com.project.os_manager.ordemservico.enums.TipoOrdemServico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrdemServicoCriacaoRequest(

        @NotNull(message = "O Equipamento é Obrigatório")
        Long equipamentoId,

        @NotBlank(message = "O Título é obrigatório")
        @Size(max = 150, message = "O Título deve ter no máximo 150 caracteres")
        String titulo,

        @Size(max = 5000, message = "A descrição deve ter no máximo 5000 caracteres")
        String descricao,

        @NotNull(message = "O tipo é Obrigatório")
        TipoOrdemServico tipo,

        PrioridadeOrdemServico prioridade
) {
}
