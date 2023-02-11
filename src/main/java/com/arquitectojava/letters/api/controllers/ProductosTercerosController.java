package com.arquitectojava.letters.api.controllers;

import com.arquitectojava.letters.api.services.ProductosTercerosService;
import com.arquitectojava.letters.api.domain.json.MarketProduct;
import com.arquitectojava.letters.api.domain.sql.Producto;
import ma.glasnost.orika.BoundMapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductosTercerosController {

    protected ProductosTercerosService productosTercerosService;
    protected BoundMapperFacade<Producto, MarketProduct> boundMapperFacade;

    public ProductosTercerosController(@Autowired ProductosTercerosService productosTercerosService, @Autowired BoundMapperFacade<Producto, MarketProduct> boundMapperFacade){
        this.productosTercerosService = productosTercerosService;
        this.boundMapperFacade = boundMapperFacade;
    }

    @GetMapping("market")
    public List<MarketProduct> list(){
        return productosTercerosService.findAll().stream().map(p -> boundMapperFacade.map(p)).collect(Collectors.toList());
    }

    @PostMapping("market")
    @ResponseStatus(HttpStatus.CREATED)
    public MarketProduct create(@RequestBody MarketProduct marketProduct) {
        Producto created = productosTercerosService.save(boundMapperFacade.mapReverse(marketProduct));
        return boundMapperFacade.map(created);
    }

    @PutMapping("market/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public MarketProduct update(@PathVariable Integer id, @RequestBody MarketProduct marketProduct) {
        Producto updated = productosTercerosService.save(boundMapperFacade.mapReverse(marketProduct));
        return boundMapperFacade.map(updated);
    }
}
