package com.project.os_manager.cliente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(

        @NotBlank(message = "O Nome é Obrigatório")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
        String nome,

        @Email(message = "E-mail Inválido")
        @Size(max = 150, message = "O -email deve ter no máximo 150 caracteres")
        String email,

        @Size(max = 20, message = "O Telefone deve ter no máximo 20 caracteres")
        String telefone,

        @Size(max = 20, message = "O Documento deve ter no máximo 20 caracteres")
        String documento
) {
}
