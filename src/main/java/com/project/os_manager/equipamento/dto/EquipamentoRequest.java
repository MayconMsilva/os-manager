package com.project.os_manager.equipamento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EquipamentoRequest(

        @NotNull(message = "O Cliente é obrigatório")
        Long clienteId,

        @NotBlank(message = "O Nome é obrigatório")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
        String nome,

        @Size(max = 80, message = "O tipo deve ter no máximo 80 caracteres")
        String tipo,

        @Size(max = 80, message = "A Marca dever ter no máximo 80 caracteres")
        String marca,

        @Size(max = 80, message = "O Modelo deve ter no máximo 80 caracteres")
        String modelo,

        @Size(max = 80, message = "O Número de Série deve ter no maximo 80 caracteres")
        String numeroSerie,

        @Size(max = 150, message = "A Localização deve ter no máximo 150 caracteres")
        String localizacao

) {
}
