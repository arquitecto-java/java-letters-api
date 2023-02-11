package com.arquitectojava.letters.api.config;

import com.arquitectojava.letters.api.clientes.domain.sql.Cliente;
import com.arquitectojava.letters.api.domain.json.*;
import com.arquitectojava.letters.api.domain.sql.*;
import ma.glasnost.orika.*;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ComponentScan("com.arquitectojava.letters.api")
public class AppConfig {

    protected MapperFactory posMapperFactory = new DefaultMapperFactory.Builder().build();

    protected MapperFactory marketMapperFactory = new DefaultMapperFactory.Builder().build();

    @Bean
    public MapperFacade posMapperFactory(){
        posMapperFactory.classMap(Cliente.class, ClienteJSON.class)
                .field("docId", "doc_id")
                .byDefault()
                .register();

        return posMapperFactory.getMapperFacade();
    }

    @Bean
    public BoundMapperFacade<Cliente, ClienteJSON> clienteToClienteJSONBoundMapperFacade(){
        BoundMapperFacade<Cliente, ClienteJSON> boundMapper =
                posMapperFactory.getMapperFacade(Cliente.class, ClienteJSON.class);

        return boundMapper;
    }
}
