package com.project.os_manager.ordemservico;

import com.project.os_manager.equipamento.Equipamento;
import com.project.os_manager.ordemservico.enums.PrioridadeOrdemServico;
import com.project.os_manager.ordemservico.enums.StatusOrdemServico;
import com.project.os_manager.ordemservico.enums.TipoOrdemServico;
import com.project.os_manager.planopreventivo.PlanoPreventivo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "ordens_servico")
@Getter
@Setter
@NoArgsConstructor
public class OrdemServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoOrdemServico tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusOrdemServico status = StatusOrdemServico.ABERTA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PrioridadeOrdemServico prioridade = PrioridadeOrdemServico.MEDIA;

    @Column(name = "aberta_em", nullable = false, updatable = false)
    private OffsetDateTime abertaEm;

    @Column(name = "concluida_em")
    private OffsetDateTime concluidaEm;

    @Column(columnDefinition = "TEXT")
    private String solucao;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_preventivo_id", updatable = false)
    private PlanoPreventivo planoPreventivo;


    @PrePersist
    void definirAbertaEm(){
        if(abertaEm == null){
            abertaEm = OffsetDateTime.now();
        }
    }
}
