package com.arquitectojava.letters.api.clientes.exceptions;

import com.arquitectojava.letters.api.clientes.domain.sql.Cliente;
import lombok.Data;

import java.util.List;

@Data
public class ClienteDuplicadoException extends RuntimeException {
    private List<Cliente> clientesExistentes;

    public ClienteDuplicadoException(List<Cliente> clientesExistentes){
        super(clientesExistentes.size() == 1 ? "Se ha encontrado un cliente con la información registrada" : "Se ha encontrado " + clientesExistentes.size() + " clientes con la información registrada");
        this.clientesExistentes = clientesExistentes;
    }
}
