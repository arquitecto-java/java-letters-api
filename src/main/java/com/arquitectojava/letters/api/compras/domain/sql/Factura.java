package com.arquitectojava.letters.api.compras.domain.sql;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "facturas")
@Data
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    @Column(name = "proveedor_id")
    private Integer proveedorId;
    @Column(name = "no_factura")
    private String numeroFactura;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @CreationTimestamp
    private Date creado;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Temporal(TemporalType.TIMESTAMP)
    private Date anulado;
    private Integer base;
    private Integer descuento;
    private Integer iva;
    private Integer total;
    private Integer pagado;
    private Integer retenido;
}