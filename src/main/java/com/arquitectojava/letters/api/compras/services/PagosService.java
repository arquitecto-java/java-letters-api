package com.arquitectojava.letters.api.compras.services;

import com.arquitectojava.letters.api.compras.domain.sql.Pago;

import java.util.List;

public interface PagosService {

    List<Pago> findAll();

    Pago save(Pago pago);

}
