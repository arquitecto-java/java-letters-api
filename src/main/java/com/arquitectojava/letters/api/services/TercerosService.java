package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.domain.sql.Tercero;

import java.util.List;

public interface TercerosService {

    List<Tercero> findAll();

    List<Tercero> findAllSuppliers();

    Tercero save(Tercero tercero);

}
