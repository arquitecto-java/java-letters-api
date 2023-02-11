package com.arquitectojava.letters.api.domain.sql;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "matches")
@Data
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Solo lectura, se utiliza para tener acceso a la sku de productoLetters
     */
    @Transient
    private Producto producto;

    @Column(name = "producto_id")
    private int productoId;

    @Column(name = "match_id")
    private int matchId;

    @Column(updatable = false)
    @CreationTimestamp
    private Date creado;

}
