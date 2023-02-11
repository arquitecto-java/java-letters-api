package com.arquitectojava.letters.api.repositories;

import com.arquitectojava.letters.api.domain.sql.Match;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MatchesRepository extends CrudRepository<Match, Integer> {

    @Query("select m from Match m where m.productoId = ?1")
    List<Match> matches(int productoId);

    @Query("select m from Match m where m.matchId = ?1")
    List<Match> reverseMatches(int matchId);

}
