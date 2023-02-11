package com.arquitectojava.letters.api.compras.services;

import com.arquitectojava.letters.api.compras.domain.sql.Compra;

import java.util.List;

public interface ComprasService {

    List<Compra> findAll();

    Compra save(Compra compra);

}
