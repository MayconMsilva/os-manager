package com.project.os_manager.ordemservico;


import com.project.os_manager.ordemservico.dto.GeracaoPreventivaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ordens-servico/preventivas")
@RequiredArgsConstructor
public class OsPreventivaController {

    private final GeracaoOsPreventivaService service;

    @PostMapping("/gerar")
    public GeracaoPreventivaResponse gerar(){
        return service.gerarVencidas();
    }
}
