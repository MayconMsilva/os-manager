package com.project.os_manager.cliente;

import com.project.os_manager.cliente.dto.ClienteRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ClienteRequestTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();


    @Test
    void deveAceitarRequestValido(){
        var request = new ClienteRequest("Maria Silva", "maria@gmail.com", "1199999999", null);

        assertThat(validator.validate(request)).isEmpty();
    }

    @Test
    void deveRejeitarNomeEmBranco(){
        var request = new ClienteRequest(" ", null, null, null);

        Set<ConstraintViolation<ClienteRequest>> violacoes = validator.validate(request);

        assertThat(violacoes).hasSize(1);
        assertThat(violacoes.iterator().next().getPropertyPath().toString()).isEqualTo("nome");
    }

    @Test
    void deveRejeitarEmailInvalido(){
        var request = new ClienteRequest("Maria", "isso-nao-e-email", null, null);

        Set<ConstraintViolation<ClienteRequest>> violacoes = validator.validate(request);

        assertThat(violacoes).hasSize(1);
        assertThat(violacoes.iterator().next().getPropertyPath().toString()).isEqualTo("email");
    }
}
