package com.arquitectojava.letters.api.exceptions;

public class ProveedorNoValidoProductoMercadoException extends RuntimeException {
    public ProveedorNoValidoProductoMercadoException(Integer proveedorId){
        super("El proveedor " + proveedorId + " no es válido como proveedor de un producto de mercado");
    }
}
