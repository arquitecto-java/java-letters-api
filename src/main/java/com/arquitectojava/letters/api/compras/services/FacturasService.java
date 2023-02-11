package com.arquitectojava.letters.api.compras.services;

import com.arquitectojava.letters.api.compras.domain.sql.Factura;

import java.util.List;

public interface FacturasService {

    List<Factura> findAll();

    Factura save(Factura factura);

}
