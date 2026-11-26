package com.project.os_manager.ordemservico;


import com.project.os_manager.equipamento.Equipamento;
import com.project.os_manager.ordemservico.enums.StatusOrdemServico;
import com.project.os_manager.ordemservico.enums.TipoOrdemServico;
import com.project.os_manager.planopreventivo.PlanoPreventivo;
import com.project.os_manager.planopreventivo.PlanoPreventivoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeradorOsPreventiva {

    private static final List<StatusOrdemServico> STATUS_EM_ABERTO =
            List.of(StatusOrdemServico.ABERTA, StatusOrdemServico.EM_ANDAMENTO);


    private final PlanoPreventivoRepository planoRepository;
    private final OrdemServicoRepository osRepository;
    private final OrdemServicoHistoricoRepository historicoRepository;
    private final Clock clock;


    @Transactional
    public boolean gerar(Long planoId){
        LocalDate hoje = LocalDate.now(clock);

        PlanoPreventivo plano = planoRepository.findComEquipamentoById(planoId).orElse(null);
        if (plano == null || !plano.isAtivo() || plano.getProximaExecucao().isAfter(hoje)){
            return false;
        }


        Equipamento equipamento = plano.getEquipamento();
        if (!equipamento.isAtivo() || !equipamento.getCliente().isAtivo()){
            log.warn("Plano {} ignorado: Equipamento ou Cliente Inativo", planoId);
        }

        if (osRepository.existsByPlanoPreventivoIdAndStatusIn(planoId, STATUS_EM_ABERTO)){
            log.info("Plano {} ignorado: Já existe OS em aberto", planoId);
            return false;
        }

        OrdemServicoEntity os = new OrdemServicoEntity();
        os.setEquipamento(equipamento);
        os.setPlanoPreventivo(plano);
        os.setTitulo(plano.getTitulo());
        os.setDescricao(plano.getDescricao());
        os.setTipo(TipoOrdemServico.PREVENTIVA);
        os.setPrioridade(plano.getPrioridade());


        OrdemServicoEntity salva = osRepository.save(os);
        historicoRepository.save(
                new OrdemServicoHistorico(salva, null, salva.getStatus(), salva.getAbertaEm()));
        plano.setProximaExecucao(hoje.plusDays(plano.getPeriodicidadeDias()));
        return true;
    }
}
