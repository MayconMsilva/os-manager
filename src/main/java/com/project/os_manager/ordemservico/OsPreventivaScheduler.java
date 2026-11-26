package com.project.os_manager.ordemservico;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OsPreventivaScheduler {

    private final GeracaoOsPreventivaService service;

    @Scheduled(cron = "${os-manager.preventivas.cron:0 0 6 * * *}", zone = "America/Sao_Paulo")
    public void executar(){
        service.gerarVencidas();
    }
}
