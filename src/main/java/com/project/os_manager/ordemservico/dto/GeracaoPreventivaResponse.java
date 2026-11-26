package com.project.os_manager.ordemservico.dto;

public record GeracaoPreventivaResponse(
        int geradas,
        int ignoradas,
        int falhas
) {
}
