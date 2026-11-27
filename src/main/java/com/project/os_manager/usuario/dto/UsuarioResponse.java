package com.project.os_manager.usuario.dto;

import com.project.os_manager.usuario.Usuario;
import com.project.os_manager.usuario.enums.PerfilUsuario;

import java.time.OffsetDateTime;

public record UsuarioResponse(

        Long id,
        String nome,
        String email,
        PerfilUsuario perfil,
        boolean ativo,
        OffsetDateTime criadoEm
) {

    public static UsuarioResponse de(Usuario usuario){
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.isAtivo(),
                usuario.getCriadoEm()
        );
    }
}
