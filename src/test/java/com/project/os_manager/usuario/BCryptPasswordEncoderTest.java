package com.project.os_manager.usuario;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;

public class BCryptPasswordEncoderTest {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();


    @Test
    void naoGuardarASenhaEmTextoPuro(){
        String hash = encoder.encode("MinhaSenha123");

        assertThat(hash).isNotEqualTo("MinhaSenha123");
        assertThat(hash).startsWith("$2");
    }

    @Test
    void mesmaSenhaGeraHashesDiferentesMasAmbosConferem(){
        String hash1 = encoder.encode("MinhaSenha123");
        String hash2 = encoder.encode("MinhaSenha123");


        assertThat(hash1).isNotEqualTo(hash2);
        assertThat(encoder.matches("MinhaSenha123", hash1)).isTrue();
        assertThat(encoder.matches("MinhaSenha123", hash2)).isTrue();
    }

    @Test
    void senhaErradaNaoConfere(){
        String hash = encoder.encode("MinhaSenha123");

        assertThat(encoder.matches("OutraSenha123", hash)).isFalse();
    }
}
