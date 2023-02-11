package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.domain.sql.Venta;

import java.util.Date;
import java.util.List;

public interface VentasService {

    List<Venta> findByCreadoBetween(Date from, Date to);

    Venta save(Venta venta);

}
