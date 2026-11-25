package com.project.os_manager.planopreventivo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlanoPreventivoRepository extends JpaRepository<PlanoPreventivo, Long> {

    @EntityGraph(attributePaths = "equipamento")
    Optional<PlanoPreventivo> findComEquipamentoById(Long id);

    @Query("""
            select p from PlanoPreventivo p
            join fetch p.equipamento e
            where(:equipamentoId is null or e.id = :equipamentoId)
            order by p.proximaExecucao asc, p.id as
            """)
    List<PlanoPreventivo> filtrar(@Param("equipamentoId") Long equipamentoId);
}
