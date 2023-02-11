package com.arquitectojava.letters.api.exceptions;

import com.arquitectojava.letters.api.domain.sql.Producto;

public class ProductoNoExisteException extends RuntimeException {
    public ProductoNoExisteException(Producto p){
        super("El producto " + p.getNombre() + " no existe");
    }

    public ProductoNoExisteException(int productoId){
        super("El producto " + productoId + " no existe");
    }

    public ProductoNoExisteException(String sku){
        super("El producto " + sku + " no existe");
    }
}
