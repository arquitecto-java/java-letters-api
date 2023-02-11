package com.arquitectojava.letters.api.clientes.controllers;

import com.arquitectojava.letters.api.clientes.domain.sql.Cliente;
import com.arquitectojava.letters.api.clientes.services.ClientesService;
import com.arquitectojava.letters.api.domain.json.Client;
import ma.glasnost.orika.BoundMapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class ClientesController {

    protected ClientesService clientesService;
    protected BoundMapperFacade<Cliente, Client> boundMapperFacade;

    public ClientesController(@Autowired ClientesService clientesService, @Autowired BoundMapperFacade<Cliente, Client> boundMapperFacade){
        this.clientesService = clientesService;
        this.boundMapperFacade = boundMapperFacade;
    }

    @GetMapping("clients")
    public List<Client> list(){
        return Collections.singletonList(new Client(){{ setFname("Lanzamiento"); setLname("AWS Serverless");}});
        //return clientesService.findAll().stream().map(c -> boundMapperFacade.map(c)).collect(Collectors.toList());
    }

    @PostMapping("clients")
    @ResponseStatus(HttpStatus.CREATED)
    public Client create(@RequestBody Client client) {
        Cliente created = clientesService.save(boundMapperFacade.mapReverse(client));
        return boundMapperFacade.map(created);
    }

    @PutMapping("clients/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Client update(@PathVariable Integer id, @RequestBody Client client) {
        Cliente created = clientesService.save(boundMapperFacade.mapReverse(client));
        return boundMapperFacade.map(created);
    }
}
