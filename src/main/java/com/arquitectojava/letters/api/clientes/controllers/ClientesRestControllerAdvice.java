package com.arquitectojava.letters.api.clientes.controllers;

import com.arquitectojava.letters.api.clientes.domain.json.ClienteDuplicadoResult;
import com.arquitectojava.letters.api.clientes.exceptions.ClienteDuplicadoException;
import com.arquitectojava.letters.api.domain.json.ResultJSON;
import com.arquitectojava.letters.api.exceptions.DatosEntidadInsuficientesException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ClientesRestControllerAdvice {

    @ExceptionHandler({ClienteDuplicadoException.class})
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    public ClienteDuplicadoResult handleInternalError(ClienteDuplicadoException e) {
        return ClienteDuplicadoResult.builder()
                .message(e.getMessage())
                .existing_clients(e.getClientesExistentes())
                .build();
    }

    @ExceptionHandler({DatosEntidadInsuficientesException.class})
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    public ResultJSON handleInternalError(DatosEntidadInsuficientesException e) {
        return ResultJSON.builder()
                .message(e.getMessage())
                .build();
    }

}