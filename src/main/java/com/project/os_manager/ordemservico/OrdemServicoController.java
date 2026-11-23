package com.project.os_manager.ordemservico;

import com.project.os_manager.ordemservico.dto.*;
import com.project.os_manager.ordemservico.enums.StatusOrdemServico;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ordens-servico")
@RequiredArgsConstructor
public class OrdemServicoController {

    private final OrdemServicoService service;

    @PostMapping
    public ResponseEntity<OrdemServicoResponse> criar(@Valid @RequestBody OrdemServicoCriacaoRequest
                                                      request){
        OrdemServicoResponse criada = service.criar(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criada.id())
                .toUri();

        return ResponseEntity.created(location).body(criada);
    }

    @GetMapping
    public List<OrdemServicoResponse> listar(@RequestParam(required = false)
                                             StatusOrdemServico status,
                                             @RequestParam(required = false) Long equipamentoId){
        return service.listar(status, equipamentoId);
    }

    @GetMapping("/{id}")
    public OrdemServicoResponse buscar(@PathVariable Long id){
        return service.buscar(id);
    }

    @GetMapping("/{id}/historico")
    public List<HistoricoStatusResponse> historico(@PathVariable Long id){
        return service.listarHistorico(id);
    }

    @PutMapping("/{id}")
    public OrdemServicoResponse atualizar(@PathVariable Long id, @Valid @RequestBody
                                            OrdemServicoAtualizacaoRequest request){

        return service.atualizar(id, request);
    }

    @PatchMapping("/{id}/status")
    public OrdemServicoResponse alterarStatus(@PathVariable Long id, @Valid @RequestBody
                                              AlteracaoStatusRequest request){
        return service.alterarStatus(id, request);
    }
}
