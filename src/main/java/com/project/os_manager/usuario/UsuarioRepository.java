package com.project.os_manager.usuario;

import com.project.os_manager.usuario.enums.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByPerfilAndAtivoTrue(PerfilUsuario perfil);
}
