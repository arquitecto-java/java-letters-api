package com.arquitectojava.letters.api.compras.controllers;

import com.arquitectojava.letters.api.compras.domain.json.Purchase;
import com.arquitectojava.letters.api.compras.domain.sql.Compra;
import com.arquitectojava.letters.api.compras.services.ComprasService;
import ma.glasnost.orika.BoundMapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ComprasController {

    protected ComprasService comprasService;
    protected BoundMapperFacade<Compra, Purchase> boundMapperFacade;

    public ComprasController(@Autowired ComprasService comprasService, @Autowired BoundMapperFacade<Compra, Purchase> boundMapperFacade){
        this.comprasService = comprasService;
        this.boundMapperFacade = boundMapperFacade;
    }

    @GetMapping("purchases")
    public List<Purchase> list(){
        return comprasService.findAll().stream().map(p -> boundMapperFacade.map(p)).collect(Collectors.toList());
    }

    @PostMapping("purchases")
    @ResponseStatus(HttpStatus.CREATED)
    public Purchase create(@RequestBody Purchase purchase) {
        Compra created = comprasService.save(boundMapperFacade.mapReverse(purchase));
        return boundMapperFacade.map(created);
    }

    @PutMapping("purchases/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Purchase update(@PathVariable Integer id, @RequestBody Purchase purchase) {
        Compra created = comprasService.save(boundMapperFacade.mapReverse(purchase));
        return boundMapperFacade.map(created);
    }
}
