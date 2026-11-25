package com.project.os_manager.ordemservico.dto;

import com.project.os_manager.ordemservico.enums.StatusOrdemServico;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AlteracaoStatusRequest(
        @NotNull(message = "O Status é Obrigatório")
        StatusOrdemServico status,

        @Size(max = 5000, message = "A solução deve ter no máximo 5000 caracteres")
        String solucao,

        @Size(max = 5000, message = "As observações devem ter no máximo 5000 caracteres")
        String observacoes
) {
}
