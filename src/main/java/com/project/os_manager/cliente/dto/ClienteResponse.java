package com.project.os_manager.cliente.dto;

import com.project.os_manager.cliente.Cliente;

import java.time.OffsetDateTime;

public record ClienteResponse(

        Long id,
        String nome,
        String email,
        String telefone,
        String documento,
        boolean ativo,
        OffsetDateTime criadoEm
) {

    public static ClienteResponse de(Cliente cliente){
      return new ClienteResponse(
              cliente.getId(),
              cliente.getNome(),
              cliente.getEmail(),
              cliente.getTelefone(),
              cliente.getDocumento(),
              cliente.isAtivo(),
              cliente.getCriadoEm()
      );
    }
}
