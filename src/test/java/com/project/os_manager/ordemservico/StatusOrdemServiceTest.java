package com.project.os_manager.ordemservico;

import com.project.os_manager.ordemservico.enums.StatusOrdemServico;
import org.junit.jupiter.api.Test;



import static com.project.os_manager.ordemservico.enums.StatusOrdemServico.ABERTA;
import static com.project.os_manager.ordemservico.enums.StatusOrdemServico.CANCELADA;
import static com.project.os_manager.ordemservico.enums.StatusOrdemServico.CONCLUIDA;
import static com.project.os_manager.ordemservico.enums.StatusOrdemServico.EM_ANDAMENTO;
import static org.assertj.core.api.Assertions.assertThat;



public class StatusOrdemServiceTest {

    @Test
    void abertaPodeIniciarOuCancelar(){
        assertThat(ABERTA.podeTransitarPara(EM_ANDAMENTO)).isTrue();
        assertThat(ABERTA.podeTransitarPara(CANCELADA)).isTrue();
    }

    @Test
    void abertaNaoPodeConcluirDiretamente(){
        assertThat(ABERTA.podeTransitarPara(CONCLUIDA)).isFalse();
    }

    @Test
    void emAndamentoPodeConcluirOuCancelar(){
        assertThat(EM_ANDAMENTO.podeTransitarPara(CONCLUIDA)).isTrue();
        assertThat(EM_ANDAMENTO.podeTransitarPara(CANCELADA)).isTrue();
    }

    @Test
    void emAndamentoNaoPodeVoltarParaAberta(){
        assertThat(EM_ANDAMENTO.podeTransitarPara(ABERTA)).isFalse();
    }

    @Test
    void estadosFinaisNaoTransitamParaNenhumStatus(){
        for(StatusOrdemServico destino : StatusOrdemServico.values()){
            assertThat(CONCLUIDA.podeTransitarPara(destino)).isFalse();
            assertThat(CANCELADA.podeTransitarPara(destino)).isFalse();
        }
    }

    @Test
    void ninguemTransitaParaOProprioStatus(){
        for (StatusOrdemServico status : StatusOrdemServico.values()){
            assertThat(status.podeTransitarPara(status)).isFalse();
        }
    }
}
