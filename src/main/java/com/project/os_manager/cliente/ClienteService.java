package com.project.os_manager.cliente;

import com.project.os_manager.cliente.dto.ClienteRequest;
import com.project.os_manager.cliente.dto.ClienteResponse;
import com.project.os_manager.exception.ConflitoException;
import com.project.os_manager.exception.RecursoNaoEncontradoException;
import jakarta.transaction.TransactionScoped;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    @Transactional
    public ClienteResponse criar(ClienteRequest request){
        String email = normalizarEmail(request.email());

        if (email != null && repository.existsByEmail(email)){
            throw new ConflitoException("Já Existe um Cliente com e-mail" + email);
        }

        Cliente cliente = new Cliente();
        preencher(cliente, request, email);
        return ClienteResponse.de(repository.save(cliente));
    }

    @Transactional
    public List<ClienteResponse> listar(String nome){
        List<Cliente> clientes = (nome == null || nome.isBlank())
                ? repository.findAll()
                : repository.findByNomeContainingIgnoreCase(nome.trim());

        return clientes.stream().map(ClienteResponse::de).toList();
    }

    @Transactional
    public ClienteResponse bucar(Long id){
        return ClienteResponse.de(buscarEntidade(id));
    }

    @Transactional
    public ClienteResponse atualizar(Long id, ClienteRequest request){
        Cliente cliente = buscarEntidade(id);
        String email = normalizarEmail(request.email());

        if (email != null && repository.existsByEmailAndIdNot(email, id)){
            throw new ConflitoException("Já Existe um cliente com o e-mail " + email);
        }

        preencher(cliente, request, email);
        return ClienteResponse.de(cliente);
    }

    @Transactional
    public void desativar(Long id){
        Cliente cliente = buscarEntidade(id);
        cliente.setAtivo(false);
    }

    private Cliente buscarEntidade(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente Não Encontrado: " + id));
    }

    private void preencher(Cliente cliente, ClienteRequest request, String email){
        cliente.setNome(request.nome().trim());
        cliente.setEmail(email);
        cliente.setTelefone(vazioParaNulo(request.telefone()));
        cliente.setDocumento(vazioParaNulo(request.documento()));
    }

    private String normalizarEmail(String email){
        String valor = vazioParaNulo(email);
        return valor == null ? null : valor.toLowerCase();
    }

    private String vazioParaNulo(String valor){
        return (valor == null || valor.isBlank() ? null : valor.trim());
    }
}
