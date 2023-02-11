package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.domain.sql.Producto;

import java.util.List;

public interface ProductosTercerosService {

    Producto findById(int id);

    List<Producto> findAll();

    Producto findBySku(String sku, int proveedorId);

    Producto save(Producto producto);

}
