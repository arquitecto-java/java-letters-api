package com.arquitectojava.letters.api.exceptions;

public class ProductoMercadoDuplicadoPorSKUException extends RuntimeException {
    public ProductoMercadoDuplicadoPorSKUException(String sku, int proveedorId){
        super("El producto con sku " + sku + " ya existe asociado al proveedor " + proveedorId);
    }
}
