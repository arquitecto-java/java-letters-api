package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.domain.sql.Match;
import com.arquitectojava.letters.api.domain.sql.Producto;

import java.util.List;

public interface MatchesService {

    List<Match> reverseMatchesByProductId(int productoId);

    List<Match> matchesBySku(String sku);

    void updateMatches(Producto p);

    void updateReverseMatches(Producto p);

}
