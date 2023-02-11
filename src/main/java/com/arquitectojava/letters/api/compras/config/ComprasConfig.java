package com.arquitectojava.letters.api.compras.config;

import com.arquitectojava.letters.api.compras.domain.json.Purchase;
import com.arquitectojava.letters.api.compras.domain.sql.Compra;
import com.arquitectojava.letters.api.compras.domain.json.Invoice;
import com.arquitectojava.letters.api.compras.domain.json.Payment;
import com.arquitectojava.letters.api.compras.domain.sql.Factura;
import com.arquitectojava.letters.api.compras.domain.sql.Pago;
import ma.glasnost.orika.*;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@ComponentScan("com.arquitectojava.lettersservices.compras")
public class ComprasConfig {

    protected MapperFactory comprasMapperFactory = new DefaultMapperFactory.Builder().build();

    @Bean
    public MapperFacade comprasMapperFactory(){
        comprasMapperFactory.classMap(Compra.class, Purchase.class)
                .byDefault()
                .field("proveedorId", "supplier.id")
                .field("descripcion", "description")
                .field("creado", "created")
                .field("comprado", "purchased")
                .field("recibido", "received")
                .field("costoTransporte", "transport_cost")
                .field("pagado", "paid")

                .customize(new CustomMapper<Compra, Purchase>() {
                    @Override
                    public void mapAtoB(Compra compra, Purchase purchase, MappingContext context) {
                        purchase.setConcepts(Arrays.stream(compra.getConceptosArray())
                                .map(id -> Purchase.Concept
                                        .builder()
                                        .id(id)
                                        .build()).collect(Collectors.toList()));
                    }

                    @Override
                    public void mapBtoA(Purchase purchase, Compra compra, MappingContext context) {
                        compra.setConceptos(purchase.getConcepts().stream()
                                .map(c -> c.getId() + "")
                                .reduce((a, b) -> a + ", " + b)
                                .get());
                    }
                })

                //.field("variantes", "variants") //Se usa custom mapper para controlar recursividad, productos hijos no deben usar variantes porque causa que hibernate genere un select por cada producto para hacer join y buscar hijos
                .register();

        return comprasMapperFactory.getMapperFacade();
    }

    @Bean
    public BoundMapperFacade<Compra, Purchase> compraToPurchaseBoundMapperFacade(){
        BoundMapperFacade<Compra, Purchase> boundMapper =
                comprasMapperFactory.getMapperFacade(Compra.class, Purchase.class);

        return boundMapper;
    }

    @Bean
    public BoundMapperFacade<Factura, Invoice> facturaToInvoiceBoundMapperFacade(){
        BoundMapperFacade<Factura, Invoice> boundMapper =
                comprasMapperFactory.getMapperFacade(Factura.class, Invoice.class);

        return boundMapper;
    }

    @Bean
    public BoundMapperFacade<Pago, Payment> pagoToPaymentBoundMapperFacade(){
        BoundMapperFacade<Pago, Payment> boundMapper =
                comprasMapperFactory.getMapperFacade(Pago.class, Payment.class);

        return boundMapper;
    }

}
