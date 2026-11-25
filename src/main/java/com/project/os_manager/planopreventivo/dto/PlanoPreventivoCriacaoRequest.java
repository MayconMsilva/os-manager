package com.project.os_manager.planopreventivo.dto;

import com.project.os_manager.ordemservico.enums.PrioridadeOrdemServico;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record PlanoPreventivoCriacaoRequest(

        @NotNull(message = "O Equipamento é Obrigatório")
        Long equipamentoId,

        @NotBlank(message = "O Título é obrigatório")
        @Size(max = 150, message = "O Título deve ter no máximo 150 carácteres")
        String titulo,

        @Size(max = 5000, message = "A descrição deve ter no máximo 5000 carácteres")
        String descricao,

        @NotNull(message = "A Periodicidade é Obrigatória")
        @Positive(message = "A Periodicidade deve ser de pelo menos 1 dia")
        @Max(value = 3650, message = "A Periodicidade deve ser de no máximo 3650 dias")
        Integer periodicidadeDias,

        LocalDate proximaExcecucao,

        PrioridadeOrdemServico prioridade


) {
}
