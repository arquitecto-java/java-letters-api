package com.arquitectojava.letters.api.clientes.domain.json;

import com.arquitectojava.letters.api.clientes.domain.sql.Cliente;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClienteDuplicadoResult {

    private String message;
    private List<Cliente> existing_clients;

}
