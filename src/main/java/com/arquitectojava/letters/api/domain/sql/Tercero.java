package com.arquitectojava.letters.api.domain.sql;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "terceros")
@Data
public class Tercero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String nombreEmpresa;
    private String nombres;
    private String apellidos;
    private String cc;
    private String nit;
    private String email;
    private String banco;
    @Column(name = "tipo_cuenta")
    private String tipoCuenta;
    @Column(name = "numero_cuenta")
    private String numeroCuenta;
    private String telefono;
    private String direccion;
    private String ciudad;
    @Column(updatable = false)
    @CreationTimestamp
    private Date creado;

    @OneToMany(mappedBy = "tercero", cascade = CascadeType.ALL)
    private List<RolTercero> roles;
}