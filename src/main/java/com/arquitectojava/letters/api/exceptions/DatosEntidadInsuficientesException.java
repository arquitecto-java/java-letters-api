package com.arquitectojava.letters.api.exceptions;

public class DatosEntidadInsuficientesException extends RuntimeException {
    public DatosEntidadInsuficientesException(String mensaje){
        super(mensaje);
    }
}
