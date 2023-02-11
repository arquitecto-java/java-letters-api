package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.domain.sql.DetalleVenta;
import com.arquitectojava.letters.api.domain.sql.Producto;
import com.arquitectojava.letters.api.domain.sql.Venta;
import com.arquitectojava.letters.api.events.VentaCreadaEvent;
import com.arquitectojava.letters.api.repositories.VentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = false)
public class VentasServiceImpl implements VentasService {

    protected VentasRepository ventasRepository;
    protected ProductosService productosService;

    protected ApplicationEventPublisher publisher;

    public VentasServiceImpl(@Autowired VentasRepository ventasRepository, @Autowired ProductosService productosService, @Autowired ApplicationEventPublisher applicationEventPublisher){
        this.ventasRepository = ventasRepository;
        this.productosService = productosService;
        this.publisher = applicationEventPublisher;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> findByCreadoBetween(Date from, Date to) {
        return ventasRepository.findByCreadoAfter(from);
    }

    @Override
    public Venta save(Venta venta) {
        for (DetalleVenta detalleVenta : venta.getDetallesVenta()){
            Producto producto = productosService.findBySku(detalleVenta.getProducto().getSku());
            detalleVenta.setProductoId(producto.getId());
            detalleVenta.setProducto(producto);
        }

        processVariants(venta.getDetallesVenta());

        Venta saved = ventasRepository.save(venta);

        publisher.publishEvent(VentaCreadaEvent.builder().venta(saved).build());

        return saved;
    }

    /**
     * Crea un DetalleVenta adicional por cada detalle de venta asociado a un producto variante.
     * El nuevo detalle de venta referencia al multiproducto, mantiene el order y el valor, pero marca este detalle como "metadata".
     * @param detallesVenta
     */
    protected void processVariants(List<DetalleVenta> detallesVenta) {
        ArrayList<DetalleVenta> detallesVentaTmp = new ArrayList();

        for (DetalleVenta detalleVenta : detallesVenta){
            Producto entitledItem = detalleVenta.getProducto().getEntitledItem();
            if (entitledItem != null){
                DetalleVenta nuevoDetalleVenta = new DetalleVenta();
                nuevoDetalleVenta.setOrden(detalleVenta.getOrden());
                nuevoDetalleVenta.setProductoId(entitledItem.getId());
                nuevoDetalleVenta.setProducto(entitledItem);
                nuevoDetalleVenta.setCantidad(detalleVenta.getCantidad());
                nuevoDetalleVenta.setValorSinIva(detalleVenta.getValorSinIva());
                nuevoDetalleVenta.setIva(detalleVenta.getIva());
                nuevoDetalleVenta.setTotal(detalleVenta.getTotal());
                nuevoDetalleVenta.setDetalleVentaReal(detalleVenta);
                detallesVentaTmp.add(nuevoDetalleVenta);
            }
        }

        detallesVenta.addAll(detallesVentaTmp);
    }
}
