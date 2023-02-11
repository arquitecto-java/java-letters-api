package com.arquitectojava.letters.api.compras.controllers;

import com.arquitectojava.letters.api.compras.domain.json.Payment;
import com.arquitectojava.letters.api.compras.domain.sql.Pago;
import com.arquitectojava.letters.api.compras.services.PagosService;
import ma.glasnost.orika.BoundMapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PagosController {

    protected PagosService pagosService;
    protected BoundMapperFacade<Pago, Payment> boundMapperFacade;

    public PagosController(@Autowired PagosService pagosService, @Autowired BoundMapperFacade<Pago, Payment> boundMapperFacade){
        this.pagosService = pagosService;
        this.boundMapperFacade = boundMapperFacade;
    }

    @GetMapping("payments")
    public List<Payment> list(){
        return pagosService.findAll().stream().map(p -> boundMapperFacade.map(p)).collect(Collectors.toList());
    }

    @PostMapping("payments")
    @ResponseStatus(HttpStatus.CREATED)
    public Payment create(@RequestBody Payment payment) {
        Pago created = pagosService.save(boundMapperFacade.mapReverse(payment));
        return boundMapperFacade.map(created);
    }

    @PutMapping("payments/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Payment update(@PathVariable Integer id, @RequestBody Payment payment) {
        Pago created = pagosService.save(boundMapperFacade.mapReverse(payment));
        return boundMapperFacade.map(created);
    }
}
