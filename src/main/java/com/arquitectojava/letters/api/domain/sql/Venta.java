package com.arquitectojava.letters.api.domain.sql;

import com.arquitectojava.letters.api.clientes.domain.sql.Cliente;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ventas")
@Data
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "cliente_id")
    private Integer clienteId;
    @ManyToOne
    @JoinColumn(name = "cliente_id", insertable = false, updatable = false)
    private Cliente cliente;
    private Integer subtotal;
    private Integer descuento;
    @Column(name = "valor_sin_iva")
    private Integer valorSinIva;
    private Integer iva;
    private Integer total;
    private Integer efectivo;
    private Integer tarjeta;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @CreationTimestamp
    private Date creado;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detallesVenta;

    @PostPersist
    public void postPersist(){
        for (DetalleVenta detalleVenta : detallesVenta){
            detalleVenta.setVentaId(id);
        }
    }
}
