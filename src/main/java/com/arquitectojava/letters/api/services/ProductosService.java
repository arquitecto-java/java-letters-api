package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.domain.sql.Producto;

import java.util.List;

public interface ProductosService {

    List<Producto> findAll();

    Producto findById(int id);

    Producto findBySku(String sku);

    Producto save(Producto producto);

    Producto saveImage(Producto producto);

}
