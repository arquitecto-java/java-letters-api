package com.arquitectojava.letters.api.controllers;

import com.arquitectojava.letters.api.services.MarcasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MarcasController {

    protected MarcasService marcasService;

    public MarcasController(@Autowired MarcasService marcasService){
        this.marcasService = marcasService;
    }

    @GetMapping("brands")
    public List<String> list(){
        return marcasService.findAll();
    }
}
