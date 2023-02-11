package com.arquitectojava.letters.api.controllers;

import com.arquitectojava.letters.api.services.ProductosService;
import com.arquitectojava.letters.api.domain.json.Offering;
import com.arquitectojava.letters.api.domain.sql.Producto;
import ma.glasnost.orika.BoundMapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductosController {

    protected ProductosService productosService;
    protected BoundMapperFacade<Producto, Offering> boundMapperFacade;

    public ProductosController(@Autowired ProductosService productosService, @Autowired BoundMapperFacade<Producto, Offering> boundMapperFacade){
        this.productosService = productosService;
        this.boundMapperFacade = boundMapperFacade;
    }

    @GetMapping("offerings")
    public List<Offering> list(){
        return productosService.findAll().stream().map(p -> boundMapperFacade.map(p)).collect(Collectors.toList());
    }

    @PostMapping("offerings")
    @ResponseStatus(HttpStatus.CREATED)
    public Offering create(@RequestBody Offering offering) {
        Producto created = productosService.save(boundMapperFacade.mapReverse(offering));
        return boundMapperFacade.map(created);
    }

    @PutMapping("offering/images/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Offering updateImage(@PathVariable Integer id, @RequestBody Offering offering) {
        offering.setId(id);
        Producto created = productosService.saveImage(boundMapperFacade.mapReverse(offering));
        return boundMapperFacade.map(created);
    }

    @PutMapping("offerings/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Offering update(@PathVariable Integer id, @RequestBody Offering offering) {
        offering.setId(id);
        Producto updated = productosService.save(boundMapperFacade.mapReverse(offering));
        return boundMapperFacade.map(updated);
    }
}
