package com.arquitectojava.letters.api.compras.services;

import com.arquitectojava.letters.api.compras.domain.sql.Factura;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacturasServiceImpl implements FacturasService {
    @Override
    public List<Factura> findAll() {
        return null;
    }

    @Override
    public Factura save(Factura factura) {
        return null;
    }
}
