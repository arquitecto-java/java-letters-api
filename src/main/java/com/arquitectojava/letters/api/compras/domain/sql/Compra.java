package com.arquitectojava.letters.api.compras.domain.sql;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "compras")
@EqualsAndHashCode
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Getter @Setter private Integer id;
    @NotBlank
    @Column(name = "proveedor_id")
    @Getter @Setter private Integer proveedorId;
    @Getter private String conceptos;
    @Getter @Setter private String descripcion;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @CreationTimestamp
    @Getter @Setter private Date creado;
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date comprado;
    @Temporal(TemporalType.TIMESTAMP)
    @Getter @Setter private Date recibido;
    @Column(name = "costo_transporte")
    @Getter @Setter private Integer costoTransporte;
    @Getter @Setter private Integer pagado;

    public void setConceptos(String conceptos) {
        if (!StringUtils.equals(this.conceptos, conceptos)){
            synchronized (this) {
                this.conceptos = conceptos;
                conceptosArray = null;
            }
        }
    }

    private Integer[] conceptosArray;

    public Integer[] getConceptosArray(){
        if (conceptosArray == null) {
            synchronized (this) {
                if (conceptos != null){
                    Stream<String> s = Arrays.stream(conceptos.split(","));
                    List<Integer> conceptosList = s.map(c -> Integer.parseInt(StringUtils.trim(c))).collect(Collectors.toList());
                    conceptosArray = conceptosList.toArray(new Integer[conceptosList.size()]);
                }
            }
        }
        return conceptosArray;
    }
}