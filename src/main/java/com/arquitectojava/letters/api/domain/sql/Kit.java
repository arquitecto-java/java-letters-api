package com.arquitectojava.letters.api.domain.sql;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "kits_productos")
@Data
public class Kit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(name = "producto_kit_id")
    private Integer productoKitId;
    @ManyToOne
    @JoinColumn(name = "producto_kit_id", insertable = false, updatable = false)
    private Producto productoKit;
    @NotNull
    @Column(name = "producto_id")
    private Integer productoId;
    @ManyToOne
    @JoinColumn(name = "producto_id", insertable = false, updatable = false)
    private Producto producto;
    @Min(value = 1, message = "La cantidad mínima a comprar es 1")
    private Integer cantidad;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @CreationTimestamp
    private Date creado;

}