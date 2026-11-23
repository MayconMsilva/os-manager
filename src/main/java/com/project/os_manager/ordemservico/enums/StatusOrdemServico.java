package com.project.os_manager.ordemservico.enums;

public enum StatusOrdemServico {
    ABERTA,
    EM_ANDAMENTO,
    CONCLUIDA,
    CANCELADA;


    public boolean podeTransitarPara(StatusOrdemServico destino){
        return switch (this){
            case ABERTA -> destino == EM_ANDAMENTO || destino == CANCELADA;
            case EM_ANDAMENTO -> destino == CONCLUIDA || destino == CANCELADA;
            case CONCLUIDA, CANCELADA -> false;
        };
    }

    public boolean isFinal(){
        return this == CONCLUIDA || this == CANCELADA;
    }
}
