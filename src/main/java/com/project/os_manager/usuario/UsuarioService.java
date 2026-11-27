package com.project.os_manager.usuario;

import com.project.os_manager.exception.ConflitoException;
import com.project.os_manager.exception.RecursoNaoEncontradoException;
import com.project.os_manager.exception.RegraNegocioException;
import com.project.os_manager.usuario.dto.AlteracaoSenhaRequest;
import com.project.os_manager.usuario.dto.UsuarioAtualizacaoRequest;
import com.project.os_manager.usuario.dto.UsuarioCriacaoRequest;
import com.project.os_manager.usuario.dto.UsuarioResponse;
import com.project.os_manager.usuario.enums.PerfilUsuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final int MAX_BYTES_SENHA = 72;

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponse criar(UsuarioCriacaoRequest request){
        String email = normalizarEmail(request.email());

        if (repository.existsByEmail(email)){
            throw new ConflitoException("Já Existe um usuário com o E-mail" + email);
        }

        validarTamanhoSenha(request.senha());

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome().trim());
        usuario.setEmail(email);
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario.setPerfil(request.perfil());

        return UsuarioResponse.de(repository.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar(){
        return repository.findAll(Sort.by("nome").ascending()).stream()
                .map(UsuarioResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscar(Long id){
        return UsuarioResponse.de(buscarEntidade(id));
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioAtualizacaoRequest request){
        Usuario usuario = buscarEntidade(id);
        exigirAtivo(usuario);

        if (usuario.getPerfil() == PerfilUsuario.ADMIN && request.perfil() != PerfilUsuario.ADMIN){
            garantirQueNaoEhUltimoAdmin(usuario);
        }

        usuario.setNome(request.nome().trim());
        usuario.setPerfil(request.perfil());

        return UsuarioResponse.de(usuario);
    }

    @Transactional
    public void alterarSenha(Long id, AlteracaoSenhaRequest request){
        Usuario usuario = buscarEntidade(id);
        exigirAtivo(usuario);
        validarTamanhoSenha(request.senha());

        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
    }

    @Transactional
    public void desativar(Long id){
        Usuario usuario = buscarEntidade(id);
        garantirQueNaoEhUltimoAdmin(usuario);
        usuario.setAtivo(false);
    }

    private Usuario buscarEntidade(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário Não Encontrado: " + id
                ));
    }

    private void exigirAtivo(Usuario usuario){
        if (!usuario.isAtivo()){
            throw new RegraNegocioException("Não é possível altera um Usuário Inativo");
        }
    }

    private void garantirQueNaoEhUltimoAdmin(Usuario usuario){
        if (usuario.getPerfil() == PerfilUsuario.ADMIN && usuario.isAtivo() &&
            repository.countByPerfilAndAtivoTrue(PerfilUsuario.ADMIN) <= 1){
            throw new RegraNegocioException("Não é Possível remover o último Administrador Ativo");
        }
    }


    private void validarTamanhoSenha(String senha){
        if (senha.getBytes(StandardCharsets.UTF_8).length > MAX_BYTES_SENHA){
            throw new RegraNegocioException(
                    "A Senha deve ter no máximo 72 bytes(acentos e símbolos ocupam mais de 1 bytes)"
            );
        }
    }

    private String normalizarEmail(String email){
        return email.trim().toLowerCase();
    }
}
