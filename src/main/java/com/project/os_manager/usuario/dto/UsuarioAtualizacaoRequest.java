package com.project.os_manager.usuario.dto;

import com.project.os_manager.usuario.enums.PerfilUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioAtualizacaoRequest(

        @NotBlank(message = "O Nome é Obrigatório")
        @Size(max = 50, message = "O Nome deve ter no máximo 50 caracteres")
        String nome,

        @NotNull(message = "O Perfil é Obrigatório")
        PerfilUsuario perfil
) {
}
