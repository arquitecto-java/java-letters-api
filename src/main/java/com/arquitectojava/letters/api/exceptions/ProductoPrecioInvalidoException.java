package com.arquitectojava.letters.api.exceptions;

import com.arquitectojava.letters.api.domain.sql.Producto;

public class ProductoPrecioInvalidoException extends RuntimeException {
    public ProductoPrecioInvalidoException(Producto p){
        super("El producto con sku " + p.getSku() + " y nombre " + p.getNombre() + " tiene precio inválido");
    }
}
