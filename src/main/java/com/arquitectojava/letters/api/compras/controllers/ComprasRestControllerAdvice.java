package com.arquitectojava.letters.api.compras.controllers;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ComprasRestControllerAdvice {

    /*@ExceptionHandler({ClienteDuplicadoException.class})
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    public ClienteDuplicadoResult handleInternalError(ClienteDuplicadoException e) {
        return ClienteDuplicadoResult.builder()
                .message(e.getMessage())
                .existing_clients(e.getClientesExistentes())
                .build();
    }

    @ExceptionHandler({DatosEntidadInsuficientesException.class})
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
    public Result handleInternalError(DatosEntidadInsuficientesException e) {
        return Result.builder()
                .message(e.getMessage())
                .build();
    }*/

}