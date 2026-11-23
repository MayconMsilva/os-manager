package com.project.os_manager.ordemservico.dto;

import com.project.os_manager.ordemservico.enums.StatusOrdemServico;
import jakarta.validation.constraints.NotNull;

public record AlteracaoStatusRequest(
        @NotNull(message = "O Status é Obrigatório")
        StatusOrdemServico status
) {
}
