package com.arquitectojava.letters.api.controllers;

import com.arquitectojava.letters.api.services.TercerosService;
import com.arquitectojava.letters.api.domain.json.Supplier;
import com.arquitectojava.letters.api.domain.sql.Tercero;
import ma.glasnost.orika.BoundMapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TercerosController {

    protected TercerosService tercerosService;
    protected BoundMapperFacade<Tercero, Supplier> boundMapperFacade;

    public TercerosController(@Autowired TercerosService tercerosService, @Autowired BoundMapperFacade<Tercero, Supplier> boundMapperFacade){
        this.tercerosService = tercerosService;
        this.boundMapperFacade = boundMapperFacade;
    }

    @GetMapping("suppliers")
    public List<Supplier> list(){
        return tercerosService.findAllSuppliers().stream().map(t -> boundMapperFacade.map(t)).collect(Collectors.toList());
    }

    /*@PostMapping("supplier")
    @ResponseStatus(HttpStatus.CREATED)
    public Person create(@RequestBody Person person) {
        Tercero created = tercerosService.save(boundMapperFacade.mapReverse(person));
        return boundMapperFacade.map(created);
    }*/

    /*@PutMapping("supplier/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Person update(@PathVariable Integer id, @RequestBody Person person) {
        Tercero created = tercerosService.save(boundMapperFacade.mapReverse(person));
        return boundMapperFacade.map(created);
    }*/
}
