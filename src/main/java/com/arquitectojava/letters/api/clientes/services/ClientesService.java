package com.arquitectojava.letters.api.clientes.services;

import com.arquitectojava.letters.api.clientes.domain.sql.Cliente;

import java.util.List;

public interface ClientesService {

    List<Cliente> findAll();

    Cliente save(Cliente cliente);

}
