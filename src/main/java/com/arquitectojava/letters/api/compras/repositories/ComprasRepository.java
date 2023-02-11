package com.arquitectojava.letters.api.compras.repositories;

import com.arquitectojava.letters.api.domain.sql.Producto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ComprasRepository extends CrudRepository<Producto, Integer> {

    //@EntityGraph(attributePaths = { "matchesInversos" }, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Producto p where p.proveedorId != 0")
    List<Producto> marketProducts();

    @EntityGraph(attributePaths = { "variantes", "entitledItem" }, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Producto p where p.proveedorId = 0 and p.isParent in (1,0)")
    List<Producto> lettersProducts();

    //@EntityGraph(attributePaths = { "matchesInversos" }, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Producto p where p.proveedorId = ?2 and sku = ?1")
    Producto marketProductBySku(String sku, int proveedorId);

    @EntityGraph(attributePaths = { "variantes", "entitledItem" }, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Producto p where p.proveedorId = 0 and sku = ?1")
    Producto lettersProductBySku(String sku);

    @Query(value = "select 1 + max(cast(sku as unsigned)) sku from productos p where p.proveedor_id = 0", nativeQuery = true)
    String nextLettersProductSku();

    @Query(value = "select distinct p.categoria from Producto p where p.proveedorId = 0 and p.categoria is not null and p.categoria != ''")
    List<String> categories();

    @Query(value = "select distinct p.marca from Producto p where p.proveedorId = 0 and p.marca is not null")
    List<String> brands();

}
