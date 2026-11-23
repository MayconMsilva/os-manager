package com.project.os_manager.ordemservico;

import com.project.os_manager.ordemservico.enums.StatusOrdemServico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "os_historico_status")
@Getter
@NoArgsConstructor
public class OrdemServicoHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordem_servico_id", nullable = false, updatable = false)
    private OrdemServicoEntity ordemServico;


    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior", length = 20, updatable = false)
    private StatusOrdemServico statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", length = 20, updatable = false)
    private StatusOrdemServico statusNovo;

    @Column(name = "alterado_em", nullable = false, updatable = false)
    private OffsetDateTime alteradoEm;

    public OrdemServicoHistorico(OrdemServicoEntity ordemServico, StatusOrdemServico statusAnterior,
                                 StatusOrdemServico statusNovo, OffsetDateTime alteradoEm){
        this.ordemServico = ordemServico;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.alteradoEm = alteradoEm;
    }
}
