package com.project.os_manager.equipamento;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    boolean existsByNumeroSerie (String numeroSerie);

    boolean existsByNumeroSerieAndIdNot(String numeroSerie, Long id);

    @Override
    @EntityGraph(attributePaths = "cliente")
    List<Equipamento> findAll();

    @EntityGraph(attributePaths = "cliente")
    List<Equipamento> findByClienteId(Long clienteId);
}
