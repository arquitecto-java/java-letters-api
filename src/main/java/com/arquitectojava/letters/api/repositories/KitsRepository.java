package com.arquitectojava.letters.api.repositories;

import com.arquitectojava.letters.api.domain.sql.Kit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KitsRepository extends CrudRepository<Kit, Integer> {

    @EntityGraph(attributePaths = { "producto" }, type = EntityGraph.EntityGraphType.FETCH)
    @Query("select k from Kit k where k.productoKitId = ?1")
    List<Kit> kitByProductoId(Integer productoId);

}
