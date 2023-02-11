package com.arquitectojava.letters.api.clientes.controllers;

import com.arquitectojava.letters.api.clientes.domain.sql.Cliente;
import com.arquitectojava.letters.api.clientes.services.ClientesService;
import com.arquitectojava.letters.api.domain.json.ClienteJSON;
import ma.glasnost.orika.BoundMapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClientesController {

    protected ClientesService clientesService;
    protected BoundMapperFacade<Cliente, ClienteJSON> boundMapperFacade;

    public ClientesController(@Autowired ClientesService clientesService, @Autowired BoundMapperFacade<Cliente, ClienteJSON> boundMapperFacade){
        this.clientesService = clientesService;
        this.boundMapperFacade = boundMapperFacade;
    }

    @GetMapping("clientes")
    public List<ClienteJSON> list(){
        return clientesService.findAll().stream().map(c -> boundMapperFacade.map(c)).collect(Collectors.toList());
    }

    @PostMapping("clientes")
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteJSON create(@RequestBody ClienteJSON client) {
        Cliente created = clientesService.save(boundMapperFacade.mapReverse(client));
        return boundMapperFacade.map(created);
    }

    @PutMapping("clientes/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteJSON update(@PathVariable Integer id, @RequestBody ClienteJSON client) {
        Cliente created = clientesService.save(boundMapperFacade.mapReverse(client));
        return boundMapperFacade.map(created);
    }
}
