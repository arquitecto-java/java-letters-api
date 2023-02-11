package com.arquitectojava.letters.api.repositories;

import com.arquitectojava.letters.api.domain.sql.Venta;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;


public interface VentasRepository extends CrudRepository<Venta, Integer> {
    @EntityGraph(attributePaths = { "detallesVenta.producto", "cliente" }, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select v from Venta v join fetch v.detallesVenta dv join fetch dv.producto where v.creado > ?1 and dv.detalleVentaReal is NULL ")
    List<Venta> findByCreadoAfter(Date from);
}
