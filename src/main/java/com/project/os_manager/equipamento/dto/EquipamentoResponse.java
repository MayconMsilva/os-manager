package com.project.os_manager.equipamento.dto;

import com.project.os_manager.equipamento.Equipamento;

import java.time.OffsetDateTime;

public record EquipamentoResponse(
        Long id,
        Long clienteId,
        String clienteNome,
        String nome,
        String tipo,
        String marca,
        String modelo,
        String numeroSerie,
        String localizacao,
        boolean ativo,
        OffsetDateTime criadoEm
) {

    public static EquipamentoResponse de(Equipamento equipamento){
        return new EquipamentoResponse(
                equipamento.getId(),
                equipamento.getCliente().getId(),
                equipamento.getCliente().getNome(),
                equipamento.getNome(),
                equipamento.getTipo(),
                equipamento.getMarca(),
                equipamento.getModelo(),
                equipamento.getNumeroSerie(),
                equipamento.getLocalizacao(),
                equipamento.isAtivo(),
                equipamento.getCriadoEm()
        );
    }
}
