package com.project.os_manager.ordemservico;

import com.project.os_manager.equipamento.Equipamento;
import com.project.os_manager.equipamento.EquipamentoRepository;
import com.project.os_manager.exception.RecursoNaoEncontradoException;
import com.project.os_manager.exception.RegraNegocioException;
import com.project.os_manager.ordemservico.dto.OrdemServicoAtualizacaoRequest;
import com.project.os_manager.ordemservico.dto.OrdemServicoCriacaoRequest;
import com.project.os_manager.ordemservico.dto.OrdemServicoResponse;
import com.project.os_manager.ordemservico.enums.StatusOrdemServico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdemServicoService {

    private final OrdemServicoRepository repository;
    private final EquipamentoRepository equipamentoRepository;

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

        return OrdemServicoResponse.de(repository.save(os));
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

        if (os.getStatus() == StatusOrdemServico.CONCLUIDA || os.getStatus() == StatusOrdemServico.CANCELADA){
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
