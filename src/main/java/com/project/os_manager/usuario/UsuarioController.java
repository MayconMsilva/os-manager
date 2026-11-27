package com.project.os_manager.usuario;

import com.project.os_manager.usuario.dto.AlteracaoSenhaRequest;
import com.project.os_manager.usuario.dto.UsuarioAtualizacaoRequest;
import com.project.os_manager.usuario.dto.UsuarioCriacaoRequest;
import com.project.os_manager.usuario.dto.UsuarioResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {


    private final UsuarioService service;

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioCriacaoRequest request){
        UsuarioResponse criado = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.id())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping
    public List<UsuarioResponse> listar(){
        return service.listar();
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscar(@PathVariable Long id){
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualizar(@PathVariable Long id, @Valid @RequestBody
                                        UsuarioAtualizacaoRequest request){
        return service.atualizar(id, request);
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> alterarSenha(@PathVariable Long id,
                                             @Valid @RequestBody AlteracaoSenhaRequest request){
        service.alterarSenha(id, request);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Void> desativar(@PathVariable Long id){
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
