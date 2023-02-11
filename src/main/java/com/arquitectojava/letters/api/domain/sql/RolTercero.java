package com.arquitectojava.letters.api.domain.sql;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@IdClass(RolTerceroId.class)
@Table(name = "roles_terceros")
@Data
public class RolTercero {

    public static enum Rol {
        EMPLEADO(1),
        PROVEEDOR_INSUMOS(2),
        PREVEEDOR_SERVICIOS(3),
        PROVEEDOR_PRODUCTOS(4);

        private final int index;

        Rol(int index) {
            this.index = index;
        }

        /*public int getInt() {
            return levelCode;
        }

        public Integer getInteger() {
            return levelCode;
        }*/
    }

    /*@Id
    @Column(name = "tercero_id")
    private Integer terceroId;*/
    @Id
    private String rol;

    @Id
    @ManyToOne
    @JoinColumn(name = "tercero_id")
    private Tercero tercero;

    @Column(updatable = false)
    @CreationTimestamp
    private Date creado;
}

@Data
class RolTerceroId implements Serializable {
    //@Id
    private Integer tercero;
    //@Id
    private String rol;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolTerceroId that = (RolTerceroId) o;
        return tercero.equals(that.tercero) &&
                rol.equals(that.rol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tercero, rol);
    }
}

