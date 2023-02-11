package com.arquitectojava.letters.api.config;

import com.arquitectojava.letters.api.domain.json.Offering;
import com.arquitectojava.letters.api.domain.sql.Atributo;
import com.arquitectojava.letters.api.domain.sql.Producto;
import ma.glasnost.orika.BoundMapperFacade;
import org.junit.jupiter.api.Test;

import java.util.*;

public class AppConfigTest {

    AppConfig appConfig = new AppConfig();

    @Test
    public void test_attributes_mapping(){
        Producto p = new Producto();
        p.setId(1);
        p.setSku("123");
        p.setNombre("Producto 1");
        List<Atributo> atributos = new ArrayList<>();
        Atributo size = new Atributo();
        size.setKey("size");
        size.setValue("Grande");
        Atributo sheetsCount = new Atributo();
        sheetsCount.setKey("sheets_count");
        sheetsCount.setValue(10);
        atributos.add(size);
        atributos.add(sheetsCount);
        p.setAtributos(atributos);

        appConfig.posMapperFactory();
        BoundMapperFacade<Producto, Offering> boundMapperFacade = appConfig.productoToOfferingBoundMapperFacade();

        Offering o = boundMapperFacade.map(p);

        System.out.println();

    }
}
