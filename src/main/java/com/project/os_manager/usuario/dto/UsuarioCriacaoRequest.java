package com.project.os_manager.usuario.dto;

import com.project.os_manager.usuario.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioCriacaoRequest(

        @NotBlank(message = "O Nome é obrigatório")
        @Size(max = 50, message = "O Nome deve ter no máximo 50 caracteres")
        String nome,

        @NotBlank(message = "O E-mail é obrigatório")
        @Email(message = "E-mail Inválido")
        @Size(max = 30, message = "O E-mail deve ter no máximo 30 caracters")
        String email,

        @NotBlank(message = "A Senha é obrigatória")
        @Size(min = 8, max = 72, message = "A Senha deve ter entre 8 e 72 caracters")
        String senha,

        @NotNull(message = "O Perfil é Obrigatório")
        PerfilUsuario perfil
) {
}
