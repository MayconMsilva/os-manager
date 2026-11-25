package com.project.os_manager.planopreventivo.dto;

import com.project.os_manager.ordemservico.enums.PrioridadeOrdemServico;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PlanoPreventivoAtualizacaoRequest(

        @NotBlank(message = "O Título é Obrigatório")
        @Size(max = 150, message = "O título deve conter no maximo 150 caracteres")
        String titulo,

        @Size(max = 5000, message = "A Descrição deve ter no maximo 5000 caracteres")
        String descricao,

        @NotNull(message = "A Periodicidade é obrigatória")
        @Positive(message = "A Periodicidade deve ser de pelo menos 1 dia")
        @Max(value = 3650, message = "A Periodicidade deve ser de no máximo 3650 dias")
        Integer periodicidadeDias,

        @NotNull(message = "A Próxima Execução é Obrigatória")
        LocalDate proximaExecucao,

        @NotNull(message = "A Prioridade é Obrigatória")
        PrioridadeOrdemServico prioridade
) {
}
