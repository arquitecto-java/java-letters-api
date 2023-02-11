package com.arquitectojava.letters.api.domain.sql;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "productos")
@Data
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer proveedorId;
    private Integer retailcompassId;
    private String sku;
    @NotBlank(message = "El nombre del producto es requerido")
    private String nombre;
    private String descripcion;
    private String marca;
    private Integer cantidadPorEmpaque;
    private String ean;
    private String categoria;
    private Integer iva;
    private Integer precio;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @CreationTimestamp
    private Date creado;
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date actualizado;
    private int estado;
    @Min(value = 0, message = "El inventario mínimo es 0")
    private Integer inventario;
    private String imagen;
    private String url;
    private String color;
    @Min(value = -1, message = "is_parent debe ser 1 (entitled_item), 0 (sin variantes) o -1 (variante)")
    @Max(value = 1, message = "is_parent debe ser 1 (entitled_item), 0 (sin variantes) o -1 (variante)")
    private int isParent;
    @Column(name = "entitled_item")
    private Integer entitledItemId;
    //
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entitled_item", insertable = false, updatable = false)
    private Producto entitledItem;
    //
    @OneToMany(mappedBy = "entitledItem", cascade = CascadeType.ALL)
    private List<Producto> variantes;
    @Column(name = "sellOnline")
    private int sellOnline;
    @Convert(/*attributeName = "data", */converter = JsonToAtributosConverter.class)
    private List<Atributo> atributos;
    //
    //@Column(name = "package_item")
    //private Integer packageItemId;
    //
    //@OneToOne
    //@JoinColumn(name = "package_item", insertable = false, updatable = false)
    //private Producto packageItem;
    /*
    * no se puede usar porque genera queries innecesarios, de pronto probar con un graph diferente y un método diferente cuando se necesite
    @OneToOne(mappedBy = "packageItem", fetch = FetchType.LAZY, optional = true)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private Producto individualItem;*/

    /*@OneToMany(mappedBy = "productoTercero", cascade = CascadeType.ALL)*/
    @Transient
    private List<Integer> matchesIds;

    @Transient
    private List<String> reverseMatchesSkus;
}