package com.arquitectojava.letters.api.controllers;

import com.arquitectojava.letters.api.services.VentasService;
import com.arquitectojava.letters.api.domain.json.Sale;
import com.arquitectojava.letters.api.domain.sql.Venta;
import ma.glasnost.orika.BoundMapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VentasController {

    protected VentasService ventasService;
    protected BoundMapperFacade<Venta, Sale> boundMapperFacade;

    public VentasController(@Autowired VentasService ventasService, @Autowired BoundMapperFacade<Venta, Sale> boundMapperFacade){
        this.ventasService = ventasService;
        this.boundMapperFacade = boundMapperFacade;
    }

    @GetMapping("sales")
    public List<Sale> list(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") Date created_gte){
        return ventasService.findByCreadoBetween(created_gte, null).stream().map(c -> boundMapperFacade.map(c)).collect(Collectors.toList());
    }

    @PostMapping("sales")
    @ResponseStatus(HttpStatus.CREATED)
    public Sale create(@RequestBody Sale sale) {
        Venta created = ventasService.save(boundMapperFacade.mapReverse(sale));
        return boundMapperFacade.map(created);
    }
}
