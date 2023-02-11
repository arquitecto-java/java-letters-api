package com.arquitectojava.letters.api.domain.sql;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "detalles_ventas")
@Data
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "venta_id")
    private Integer ventaId;
    @ManyToOne
    @JoinColumn(name = "venta_id", insertable = false, updatable = false)
    private Venta venta;
    private int orden;
    @Column(name = "producto_id")
    private Integer productoId;
    @ManyToOne
    @JoinColumn(name = "producto_id", insertable = false, updatable = false)
    private Producto producto;
    @Min(value = 1, message = "La cantidad mínima a comprar es 1")
    private Integer cantidad;
    @Column(name = "valor_sin_iva")
    private Integer valorSinIva;
    private Integer iva;
    private Integer total;
    @ManyToOne
    @JoinColumn(name = "detalle_venta_real_id", insertable = true, updatable = false)
    private DetalleVenta detalleVentaReal;
}
