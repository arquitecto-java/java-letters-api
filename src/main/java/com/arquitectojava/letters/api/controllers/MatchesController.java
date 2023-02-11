package com.arquitectojava.letters.api.controllers;

import com.arquitectojava.letters.api.services.MatchesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MatchesController {

    protected MatchesService matchesService;

    public MatchesController(@Autowired MatchesService matchesService){
        this.matchesService = matchesService;
    }

    @GetMapping("reverse_matches/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<String> reverseMatches(@PathVariable int id){
        return matchesService.reverseMatchesByProductId(id).stream().map(m -> m.getProducto().getSku()).collect(Collectors.toList());
    }

    @GetMapping("matches/{sku}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> matches(@PathVariable String sku){
        return matchesService.matchesBySku(sku).stream().map(m -> m.getMatchId()).collect(Collectors.toList());
    }

}
