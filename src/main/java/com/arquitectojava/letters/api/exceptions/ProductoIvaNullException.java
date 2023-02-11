package com.arquitectojava.letters.api.exceptions;

import com.arquitectojava.letters.api.domain.sql.Producto;

public class ProductoIvaNullException extends RuntimeException {
    public ProductoIvaNullException(Producto p){
        super("El producto con sku " + p.getSku() + " y nombre " + p.getNombre() + " tiene iva null");
    }
}
