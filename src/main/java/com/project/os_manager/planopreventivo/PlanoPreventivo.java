package com.project.os_manager.planopreventivo;

import com.project.os_manager.equipamento.Equipamento;
import com.project.os_manager.ordemservico.enums.PrioridadeOrdemServico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "planos_preventivos")
@Getter
@Setter
@NoArgsConstructor
public class PlanoPreventivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipamento_id", nullable = false, updatable = false)
    private Equipamento equipamento;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "periodicidade_dias", nullable = false)
    private int periodicidadeDias;

    @Column(name = "proxima_execucao", nullable = false)
    private LocalDate proximaExecucao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PrioridadeOrdemServico prioridade = PrioridadeOrdemServico.MEDIA;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private OffsetDateTime criadoEm;

    @PrePersist
    void definirCriadoEm(){
        if (criadoEm == null){
            criadoEm = OffsetDateTime.now();
        }
    }
}
