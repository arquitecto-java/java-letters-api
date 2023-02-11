package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.repositories.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class CategoriasServiceImpl implements CategoriasService {

    protected ProductosRepository productosRepository;

    protected ApplicationEventPublisher publisher;

    public CategoriasServiceImpl(@Autowired ProductosRepository productosRepository){
        this.productosRepository = productosRepository;
    }

    @Override
    public List<String> findAll() {
        return productosRepository.categories();
    }
}
