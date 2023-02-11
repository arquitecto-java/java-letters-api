package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.domain.sql.DetalleVenta;
import com.arquitectojava.letters.api.domain.sql.Kit;
import com.arquitectojava.letters.api.events.VentaCreadaEvent;
import com.arquitectojava.letters.api.repositories.KitsRepository;
import com.arquitectojava.letters.api.repositories.VentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = false)
public class VentasKitsServiceImpl implements VentasKitsService {

    protected VentasRepository ventasRepository;
    protected KitsRepository kitsRepository;

    protected ApplicationEventPublisher publisher;

    public VentasKitsServiceImpl(@Autowired VentasRepository ventasRepository, @Autowired KitsRepository kitsRepository){
        this.ventasRepository = ventasRepository;
        this.kitsRepository = kitsRepository;
    }

    @EventListener
    public void updateVentaKit(VentaCreadaEvent event) {
        List<DetalleVenta> detallesVentaAdicionales = new ArrayList<>();
        List<DetalleVenta> detallesVenta = event.getVenta().getDetallesVenta();
        for (DetalleVenta detalleVenta : detallesVenta){
            List<Kit> kitVendido = kitsRepository.kitByProductoId(detalleVenta.getProductoId());
            if (kitVendido != null && !kitVendido.isEmpty()){
                for (Kit parKit : kitVendido){
                    DetalleVenta dtAdicional = new DetalleVenta();
                    dtAdicional.setVentaId(event.getVenta().getId());
                    dtAdicional.setOrden(detallesVenta.size() + detallesVentaAdicionales.size() + 1);
                    dtAdicional.setProductoId(parKit.getProductoId());
                    dtAdicional.setCantidad(parKit.getCantidad() * detalleVenta.getCantidad());
                    dtAdicional.setValorSinIva(0);
                    dtAdicional.setIva(0);
                    dtAdicional.setTotal(0);
                    detallesVentaAdicionales.add(dtAdicional);
                }
            }
        }

        event.getVenta().getDetallesVenta().addAll(detallesVentaAdicionales);

        ventasRepository.save(event.getVenta());
    }

}
