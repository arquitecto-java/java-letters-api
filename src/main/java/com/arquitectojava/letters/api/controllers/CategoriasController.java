package com.arquitectojava.letters.api.controllers;

import com.arquitectojava.letters.api.services.CategoriasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoriasController {

    protected CategoriasService categoriasService;

    public CategoriasController(@Autowired CategoriasService categoriasService){
        this.categoriasService = categoriasService;
    }

    @GetMapping("categories")
    public List<String> list(){
        return categoriasService.findAll();
    }
}
