package com.project.os_manager.ordemservico.dto;

import com.project.os_manager.ordemservico.enums.PrioridadeOrdemServico;
import com.project.os_manager.ordemservico.enums.TipoOrdemServico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrdemServicoAtualizacaoRequest(

        @NotBlank(message = "O título é obrigatório")
        @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
        String titulo,

        @Size(max = 5000, message = "A descriçãp deve ter no máximo 5000 caracteres")
        String descricao,

        @NotNull(message = "O Tipo é obrigatório")
        TipoOrdemServico tipo,

        @NotNull(message = "A prioridade é obrigatória")
        PrioridadeOrdemServico prioridade
) {
}
