package com.arquitectojava.letters.api.repositories;

import com.arquitectojava.letters.api.domain.sql.Tercero;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TercerosRepository extends CrudRepository<Tercero, Integer> {

    @Query("select t from Tercero t where t.id != 0")
    List<Tercero> terceros();

    @Query("select distinct t from Tercero t join fetch t.roles r where r.rol = 'PROVEEDOR_INSUMOS' or r.rol = 'PROVEEDOR_PRODUCTOS'")
    List<Tercero> suppliers();

}
