package com.arquitectojava.letters.api.compras.domain.sql;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "pagos")
@Data
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    @Column(name = "proveedor_id")
    private Integer proveedorId;
    @Column(name = "factura_id")
    private String facturaId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @CreationTimestamp
    private Date creado;
    @Temporal(TemporalType.TIMESTAMP)
    private Date pagado;
    @Column(name = "en_efectivo")
    private Integer enEfectivo;
    @Column(name = "en_chque")
    private Integer enCheque;
    private String numeroCheque;
    @Column(name = "en_cuenta")
    private Integer enCuenta;
    @Column(name = "banco_cuenta_id")
    private Integer bancoCuentaId;
    @Column(name = "numero_cuenta")
    private String numeroCuenta;
    private Integer retenido;

}