package com.project.os_manager.exception;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> tratarValidacao(MethodArgumentNotValidException ex,
                                                    HttpServletRequest request){
        List<ApiError.CampoInvalido> campos = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> new ApiError.CampoInvalido(erro.getField(), erro.getDefaultMessage()))
                .toList();

        return resposta(HttpStatus.BAD_REQUEST, "Dados Inválidos", request, campos);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiError> tratarNaoEncontrado(RecursoNaoEncontradoException ex,
                                                   HttpServletRequest request){
        return resposta(HttpStatus.NOT_FOUND,ex.getMessage(), request, null);
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ApiError> tratarConflito(ConflitoException ex,
                                                   HttpServletRequest request){
        return resposta(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ApiError> tratarRegradeNegocio(RegraNegocioException ex,
                                                         HttpServletRequest request){
        return resposta(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage(), request, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> tratarCorpoInválido(HttpMessageNotReadableException ex,
                                                        HttpServletRequest request){
        return resposta(HttpStatus.BAD_REQUEST,
                "Corpo da Requisição ausente ou malformado", request, null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> tratarTipoInválido(MethodArgumentTypeMismatchException ex,
                                                       HttpServletRequest request){
        String mensagem = "Valor Inválido para o parâmetro '" + ex.getName() + "': " + ex.getValue();
        return resposta(HttpStatus.BAD_REQUEST, mensagem, request, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> tratarIntegridade(DataIntegrityViolationException ex,
                                                      HttpServletRequest request){
        log.warn("Violação de Integridade em {}: {}", request.getRequestURI(), ex.getMostSpecificCause()
                .getMessage());

        return resposta(HttpStatus.CONFLICT,
                "A operação viola uma regra de integridade dos dados", request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> tratarErroInesperado(Exception ex, HttpServletRequest request){
        log.error("Erro Inesperado em {}", request.getRequestURI(), ex);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro Interno. Tente mais tarde.", request, null);
    }

    private ResponseEntity<ApiError> resposta(HttpStatus status, String mensagem,
                                              HttpServletRequest request,
                                              List<ApiError.CampoInvalido> campos){
        ApiError corpo = ApiError.de(status, mensagem, request.getRequestURI(), campos);
        return ResponseEntity.status(status).body(corpo);
    }
}
