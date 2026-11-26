package com.project.os_manager.planopreventivo;

import com.project.os_manager.planopreventivo.dto.PlanoPreventivoAtualizacaoRequest;
import com.project.os_manager.planopreventivo.dto.PlanoPreventivoCriacaoRequest;
import com.project.os_manager.planopreventivo.dto.PlanoPreventivoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/planos-preventivos")
@RequiredArgsConstructor
public class PlanoPreventivoController {

    private final PlanoPreventivoService service;

    @PostMapping
    public ResponseEntity<PlanoPreventivoResponse> criar(@Valid @RequestBody
                                                         PlanoPreventivoCriacaoRequest request){

        PlanoPreventivoResponse criado = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.id())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping
    public List<PlanoPreventivoResponse> listar(@RequestParam(required = false) Long equipamentoId){

        return service.listar(equipamentoId);
    }

    @GetMapping("/{id}")
    public PlanoPreventivoResponse buscar(@PathVariable Long id){
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public PlanoPreventivoResponse atualizar(@PathVariable Long id, @Valid @RequestBody
                                             PlanoPreventivoAtualizacaoRequest request){

        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id){

        service.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
