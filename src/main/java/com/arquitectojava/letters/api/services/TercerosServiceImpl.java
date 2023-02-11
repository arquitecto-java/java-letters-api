package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.domain.sql.Tercero;
import com.arquitectojava.letters.api.repositories.TercerosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class TercerosServiceImpl implements TercerosService {

    protected TercerosRepository tercerosRepository;

    protected ApplicationEventPublisher publisher;

    public TercerosServiceImpl(@Autowired TercerosRepository tercerosRepository){
        this.tercerosRepository = tercerosRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tercero> findAll() {
        return tercerosRepository.terceros();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tercero> findAllSuppliers() {
        return tercerosRepository.suppliers();
    }

    @Override
    public Tercero save(Tercero tercero) {
        //En caso de adicionar lógica implementar tests
        return tercerosRepository.save(tercero);
    }
}
