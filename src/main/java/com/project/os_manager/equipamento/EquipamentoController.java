package com.project.os_manager.equipamento;

import com.project.os_manager.equipamento.dto.EquipamentoRequest;
import com.project.os_manager.equipamento.dto.EquipamentoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/equipamentos")
@RequiredArgsConstructor
public class EquipamentoController {

    private final EquipamentoService service;

    @PostMapping
    public ResponseEntity<EquipamentoResponse> criar(@Valid @RequestBody EquipamentoRequest request){
        EquipamentoResponse criado = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path(("/{id}"))
                .buildAndExpand(criado.id())
                .toUri();

        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping
    public List<EquipamentoResponse> listar(@RequestParam(required = false) Long clienteId){
        return service.listar(clienteId);
    }

    @GetMapping("/{id}")
    public EquipamentoResponse buscar(@PathVariable Long id){
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public EquipamentoResponse atualizar(@PathVariable Long id, @Valid @RequestBody
                                         EquipamentoRequest request){

        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id){
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
