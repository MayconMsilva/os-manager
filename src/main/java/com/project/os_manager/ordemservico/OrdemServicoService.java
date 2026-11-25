package com.project.os_manager.ordemservico;

import com.project.os_manager.equipamento.Equipamento;
import com.project.os_manager.equipamento.EquipamentoRepository;
import com.project.os_manager.exception.RecursoNaoEncontradoException;
import com.project.os_manager.exception.RegraNegocioException;
import com.project.os_manager.ordemservico.dto.*;
import com.project.os_manager.ordemservico.enums.StatusOrdemServico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdemServicoService {

    private final OrdemServicoRepository repository;
    private final EquipamentoRepository equipamentoRepository;
    private final OrdemServicoHistoricoRepository historicoRepository;

    @Transactional
    public OrdemServicoResponse criar(OrdemServicoCriacaoRequest request){
        Equipamento equipamento = buscarEquipamentoElegivel(request.equipamentoId());

        OrdemServicoEntity os = new OrdemServicoEntity();
        os.setEquipamento(equipamento);
        os.setTitulo(request.titulo().trim());
        os.setDescricao(vazioParaNulo(request.descricao()));
        os.setTipo(request.tipo());
        if (request.prioridade() != null){
            os.setPrioridade(request.prioridade());
        }

        OrdemServicoEntity salva = repository.save(os);
        registrarHistorico(salva, null, salva.getStatus(), salva.getAbertaEm());

        return OrdemServicoResponse.de(salva);
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoResponse> listar(StatusOrdemServico status, Long equipamentoId){
        return repository.filtrar(status, equipamentoId).stream()
                .map(OrdemServicoResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrdemServicoResponse buscar(Long id){
        return OrdemServicoResponse.de(buscarEntidade(id));
    }

    @Transactional
    public OrdemServicoResponse atualizar(Long id, OrdemServicoAtualizacaoRequest request){
        OrdemServicoEntity os = buscarEntidade(id);

        if (os.getStatus().isFinal()){
            throw new RegraNegocioException(
                    "Não é Possível Alterar Uma Ordem de Serviço " + os.getStatus()
            );
        }

        os.setTitulo(request.titulo().trim());
        os.setDescricao(vazioParaNulo(request.descricao()));
        os.setTipo(request.tipo());
        os.setPrioridade(request.prioridade());

        return OrdemServicoResponse.de(os);
    }

    @Transactional
    public OrdemServicoResponse alterarStatus(Long id, AlteracaoStatusRequest request){
        OrdemServicoEntity os = buscarEntidade(id);
        StatusOrdemServico atual = os.getStatus();
        StatusOrdemServico novo = request.status();

        if (!atual.podeTransitarPara(novo)){
            throw new RegraNegocioException(
                    "Transição de Status Inválido: " + atual + "->" +novo
            );
        }

        String solucao = vazioParaNulo(request.solucao());
        String observacoes = vazioParaNulo(request.observacoes());


        if (novo == StatusOrdemServico.CONCLUIDA){
            if (solucao == null){
                throw new RegraNegocioException(
                        "Informe a Solução aplicada para concluir a Ordem de Serviço"
                );
            }
        }else if (solucao != null || observacoes != null){
            throw new RegraNegocioException(
                    "Solução e Observações só podem ser informadas ao concluir a Ordem de Serviço"
            );
        }

        OffsetDateTime agora = OffsetDateTime.now();
        os.setStatus(novo);
        if (novo == StatusOrdemServico.CONCLUIDA){
            os.setSolucao(solucao);
            os.setObservacoes(observacoes);
            os.setConcluidaEm(agora);
        }
        registrarHistorico(os, atual, novo, agora);
        return OrdemServicoResponse.de(os);
    }

    @Transactional(readOnly = true)
    public List<HistoricoStatusResponse> listarHistorico(Long id){
        if (!repository.existsById(id)){
            throw new RecursoNaoEncontradoException("Ordem de Serviço não Encontrado: " + id);
        }

        return historicoRepository.findByOrdemServicoIdOrderByAlteradoEmAscIdAsc(id).stream()
                .map(HistoricoStatusResponse::de)
                .toList();
    }


    private void registrarHistorico(OrdemServicoEntity os, StatusOrdemServico anterior,
                                    StatusOrdemServico novo, OffsetDateTime quando){

        historicoRepository.save(new OrdemServicoHistorico(os, anterior, novo, quando));
    }

    private OrdemServicoEntity buscarEntidade(Long id){
        return repository.findComDetalhesById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Ordem de Serviço Não Encontrada: " + id
                ));
    }

    private Equipamento buscarEquipamentoElegivel(Long equipamentoId){
        Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Equipamento Não Encontrado: " + equipamentoId
                ));

        if (!equipamento.isAtivo()){
            throw new RegraNegocioException("O Equipamento " + equipamentoId + "Está inativo");
        }
        if (!equipamento.getCliente().isAtivo()){
            throw new RegraNegocioException(
                    "O Cliente do Equipamento " + equipamentoId + "está inativo"
            );
        }
        return equipamento;
    }

    private String vazioParaNulo(String valor){
        return (valor == null || valor.isBlank()) ? null : valor.trim();
    }
}
