package com.project.os_manager.ordemservico;

import com.project.os_manager.ordemservico.enums.StatusOrdemServico;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OrdemServicoRepository extends JpaRepository<OrdemServicoEntity, Long> {

    @EntityGraph(attributePaths = {"equipamento", "equipamento.cliente"})
    Optional<OrdemServicoEntity> findComDetalhesById(Long id);


    @Query("""
            select os from OrdemServicoEntity os
            join fetch os.equipamento e
            join fetch e.cliente
            where(:status is null or os.status = :status)
                and(:equipamentoId is null or e.id = :equipamentoId)
                order by os.abertaEm desc
            """)
    List<OrdemServicoEntity> filtrar(@Param("status")StatusOrdemServico status,
                                     @Param("equipamentoId") Long equipamentoId);


    boolean existsByPlanoPreventivoIdAndStatusIn(Long planoId, Collection<StatusOrdemServico> status);
}
