package com.arquitectojava.letters.api.compras.reports.controllers;

import com.arquitectojava.letters.api.compras.reports.domain.json.CertificadoRetencion;
import com.arquitectojava.letters.api.compras.reports.services.CertificadoRetencionService;
import org.springframework.web.bind.annotation.*;

@RestController
public class CertificadoRetencionController {

    protected CertificadoRetencionService certificadoRetencionService;
    //protected BoundMapperFacade<Compra, Purchase> boundMapperFacade;

    /*public CertificadoRetencionController(@Autowired ComprasService comprasService, @Autowired BoundMapperFacade<Compra, Purchase> boundMapperFacade){
        this.comprasService = comprasService;
        this.boundMapperFacade = boundMapperFacade;
    }*/

    @GetMapping("certificado_retencion/{anio}/{supplierId}")
    public CertificadoRetencion generar(){
        return certificadoRetencionService.generar();
        //.stream().map(p -> boundMapperFacade.map(p)).collect(Collectors.toList());
    }

    /*@PostMapping("purchases")
    @ResponseStatus(HttpStatus.CREATED)
    public Purchase create(@RequestBody Purchase purchase) {
        Compra created = comprasService.save(boundMapperFacade.mapReverse(purchase));
        return boundMapperFacade.map(created);
    }*/
}
