package com.project.os_manager.ordemservico;

import com.project.os_manager.ordemservico.dto.GeracaoPreventivaResponse;
import com.project.os_manager.planopreventivo.PlanoPreventivoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeracaoOsPreventivaService {

    private final PlanoPreventivoRepository planoRepository;
    private final GeradorOsPreventiva gerador;
    private final Clock clock;


    public GeracaoPreventivaResponse gerarVencidas(){
        List<Long> ids = planoRepository.findIdsVencidos(LocalDate.now(clock));

        int geradas = 0;
        int ignoradas = 0;
        int falhas = 0;

        for (Long id : ids){
            try{
                if (gerador.gerar(id)){
                    geradas++;
                }else {
                    ignoradas++;
                }
            } catch (Exception e){
                falhas++;
                log.error("Falha ao gerar OS Preventiva do Plano {}", id, e);
            }
        }

        log.info("Geração de Os Preventivas: {} geradas, {} ignoradas, {} falhas",
                geradas, ignoradas, falhas);
        return new GeracaoPreventivaResponse(geradas, ignoradas, falhas);
    }
}
