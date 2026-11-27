package com.project.os_manager.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlteracaoSenhaRequest(

        @NotBlank(message = "A Senha é Obrigatória")
        @Size(min = 8, max = 72, message = "A Senha deve ter entre 8 e 72 caracteres")
        String senha
) {
}
