package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.repositories.KitsRepository;
import com.arquitectojava.letters.api.repositories.VentasRepository;
import com.arquitectojava.letters.api.domain.sql.DetalleVenta;
import com.arquitectojava.letters.api.domain.sql.Kit;
import com.arquitectojava.letters.api.domain.sql.Producto;
import com.arquitectojava.letters.api.domain.sql.Venta;
import com.arquitectojava.letters.api.events.VentaCreadaEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VentasKitsServiceImplTest {

    private static final int PRODUCTO_KIT_ID = 5;
    private static final int PRODUCTO_SENCILLO_ID = 3;
    private static final int DETALLE_VENTA_2 = 2;
    private static final int DETALLE_VENTA_4 = 4;
    @InjectMocks
    VentasKitsServiceImpl ventasKitsService;

    @Mock
    private VentasRepository ventasRepository;

    @Mock
    private KitsRepository kitsRepository;

    @Test
    @DisplayName("Venta de productos sencillos")
    public void test_ventas_productos_sencillos() throws Exception {
        //Behavior
        Venta venta = getVenta();
        long creado = venta.getCreado().getTime();

        List<DetalleVenta> detalleVentas = new ArrayList<>();
        DetalleVenta dv = getDetalleVentaProductoSencillo(venta);
        venta.setDetallesVenta(Arrays.asList(dv));
        setTotalesVenta(venta);

        // SUT and Assert
        ventasKitsService.updateVentaKit(VentaCreadaEvent.builder().venta(venta).build());

        //Verify
        ArgumentCaptor<Venta> argumentCaptor = ArgumentCaptor.forClass(Venta.class);
        verify(ventasRepository, times(1)).save(argumentCaptor.capture());
        Assertions.assertEquals(1, argumentCaptor.getValue().getId(), "Se ha intentado actualizar una venta diferente a que generó el evento");
        Assertions.assertEquals(100, argumentCaptor.getValue().getValorSinIva(), "Se ha intentado cambiar el valor sin iva de la venta");
        Assertions.assertEquals(19, argumentCaptor.getValue().getIva(), "Se ha intentado cambiar el iva de la venta");
        Assertions.assertEquals(119, argumentCaptor.getValue().getTotal(), "Se ha intentado cambiar el valor total de la venta");
        Assertions.assertEquals(119, argumentCaptor.getValue().getEfectivo(), "Se ha intentado cambiar el valor pagado con efectivo de la venta");
        Assertions.assertEquals(0, argumentCaptor.getValue().getTarjeta(), "Se ha intentado cambiar el valor pagado con tarjeta de la venta");
        Assertions.assertEquals(creado, argumentCaptor.getValue().getCreado().getTime(), "Se ha intentado cambiar la fecha de creado de la venta");

        Assertions.assertEquals(1, argumentCaptor.getValue().getDetallesVenta().size(), "Se ha intentado modificar los detalles de la venta");
        Assertions.assertEquals(2, argumentCaptor.getValue().getDetallesVenta().get(0).getId(), "Se ha intentado actualizar un detalle de venta diferente al incluido en la venta que generó el evento");
        Assertions.assertEquals(1, argumentCaptor.getValue().getDetallesVenta().get(0).getVentaId(), "Se ha intentado modificar la venta que referncia el detalle de la venta");
        Assertions.assertEquals(1, argumentCaptor.getValue().getDetallesVenta().get(0).getOrden(), "Se ha intentado cambiar el orden de la detalle de la venta");
        Assertions.assertEquals(PRODUCTO_SENCILLO_ID, argumentCaptor.getValue().getDetallesVenta().get(0).getProductoId(), "Se ha intentado el producto referenciado por el detalle de la venta");
        Assertions.assertEquals(1, argumentCaptor.getValue().getDetallesVenta().get(0).getCantidad(), "Se ha intentado la cantidad de productos vendidos en el detalle de la venta");
        Assertions.assertEquals(100, argumentCaptor.getValue().getDetallesVenta().get(0).getValorSinIva(), "Se ha intentado cambiar el valor sin iva en el detalle de la venta");
        Assertions.assertEquals(19, argumentCaptor.getValue().getDetallesVenta().get(0).getIva(), "Se ha intentado cambiar el iva en el detalle de la venta");
        Assertions.assertEquals(119, argumentCaptor.getValue().getDetallesVenta().get(0).getTotal(), "Se ha intentado cambiar el valor total en el detalle de la venta");
    }

    @Test
    @DisplayName("Venta de productos kits")
    public void test_ventas_productos_kits() throws Exception {
        //Behavior
        Venta venta = getVenta();
        long creado = venta.getCreado().getTime();

        List<DetalleVenta> detalleVentas = new ArrayList<>();
        DetalleVenta dv1 = getDetalleVentaProductoSencillo(venta);
        DetalleVenta dv2 = getDetalleVentaProductoKit(venta);
        ArrayList<DetalleVenta> detallesVenta = new ArrayList<>();
        detallesVenta.add(dv1);
        detallesVenta.add(dv2);
        venta.setDetallesVenta(detallesVenta);
        setTotalesVenta(venta);

        Producto p = new Producto();
        p.setId(PRODUCTO_SENCILLO_ID);
        p.setPrecio(119);
        p.setPrecio(19);

        Kit kit = new Kit();
        kit.setId(5);
        kit.setProductoKitId(PRODUCTO_KIT_ID);
        kit.setProductoId(PRODUCTO_SENCILLO_ID);
        kit.setCantidad(11);
        when(kitsRepository.kitByProductoId(PRODUCTO_SENCILLO_ID)).thenReturn(null);
        when(kitsRepository.kitByProductoId(PRODUCTO_KIT_ID)).thenReturn(Arrays.asList(kit));

        // SUT and Assert
        ventasKitsService.updateVentaKit(VentaCreadaEvent.builder().venta(venta).build());

        //Verify
        ArgumentCaptor<Venta> argumentCaptor = ArgumentCaptor.forClass(Venta.class);
        verify(ventasRepository, times(1)).save(argumentCaptor.capture());
        Assertions.assertEquals(1, argumentCaptor.getValue().getId(), "Se ha intentado actualizar una venta diferente a que generó el evento");
        Assertions.assertEquals(1100, argumentCaptor.getValue().getValorSinIva(), "Se ha intentado cambiar el valor sin iva de la venta");
        Assertions.assertEquals(209, argumentCaptor.getValue().getIva(), "Se ha intentado cambiar el iva de la venta");
        Assertions.assertEquals(1309, argumentCaptor.getValue().getTotal(), "Se ha intentado cambiar el valor total de la venta");
        Assertions.assertEquals(1309, argumentCaptor.getValue().getEfectivo(), "Se ha intentado cambiar el valor pagado con efectivo de la venta");
        Assertions.assertEquals(0, argumentCaptor.getValue().getTarjeta(), "Se ha intentado cambiar el valor pagado con tarjeta de la venta");
        Assertions.assertEquals(creado, argumentCaptor.getValue().getCreado().getTime(), "Se ha intentado cambiar la fecha de creado de la venta");

        Assertions.assertEquals(3, argumentCaptor.getValue().getDetallesVenta().size(), "No se se ha adicionado los detalles de la venta esperados");
        Assertions.assertEquals(DETALLE_VENTA_2, argumentCaptor.getValue().getDetallesVenta().get(0).getId(), "Se ha intentado actualizar un detalle de venta diferente al incluido en la venta que generó el evento");
        Assertions.assertEquals(1, argumentCaptor.getValue().getDetallesVenta().get(0).getVentaId(), "Se ha intentado modificar la venta que referncia el detalle de la venta");
        Assertions.assertEquals(1, argumentCaptor.getValue().getDetallesVenta().get(0).getOrden(), "Se ha intentado cambiar el orden de la detalle de la venta");
        Assertions.assertEquals(PRODUCTO_SENCILLO_ID, argumentCaptor.getValue().getDetallesVenta().get(0).getProductoId(), "Se ha intentado el producto referenciado por el detalle de la venta");
        Assertions.assertEquals(1, argumentCaptor.getValue().getDetallesVenta().get(0).getCantidad(), "Se ha intentado la cantidad de productos vendidos en el detalle de la venta");
        Assertions.assertEquals(100, argumentCaptor.getValue().getDetallesVenta().get(0).getValorSinIva(), "Se ha intentado cambiar el valor sin iva en el detalle de la venta");
        Assertions.assertEquals(19, argumentCaptor.getValue().getDetallesVenta().get(0).getIva(), "Se ha intentado cambiar el iva en el detalle de la venta");
        Assertions.assertEquals(119, argumentCaptor.getValue().getDetallesVenta().get(0).getTotal(), "Se ha intentado cambiar el valor total en el detalle de la venta");

        Assertions.assertEquals(DETALLE_VENTA_4, argumentCaptor.getValue().getDetallesVenta().get(1).getId(), "Se ha intentado actualizar un detalle de venta diferente al incluido en la venta que generó el evento");
        Assertions.assertEquals(1, argumentCaptor.getValue().getDetallesVenta().get(1).getVentaId(), "Se ha intentado modificar la venta que referncia el detalle de la venta");
        Assertions.assertEquals(2, argumentCaptor.getValue().getDetallesVenta().get(1).getOrden(), "Se ha intentado cambiar el orden de la detalle de la venta");
        Assertions.assertEquals(PRODUCTO_KIT_ID, argumentCaptor.getValue().getDetallesVenta().get(1).getProductoId(), "Se ha intentado el producto referenciado por el detalle de la venta");
        Assertions.assertEquals(1, argumentCaptor.getValue().getDetallesVenta().get(1).getCantidad(), "Se ha intentado la cantidad de productos vendidos en el detalle de la venta");
        Assertions.assertEquals(1000, argumentCaptor.getValue().getDetallesVenta().get(1).getValorSinIva(), "Se ha intentado cambiar el valor sin iva en el detalle de la venta");
        Assertions.assertEquals(190, argumentCaptor.getValue().getDetallesVenta().get(1).getIva(), "Se ha intentado cambiar el iva en el detalle de la venta");
        Assertions.assertEquals(1190, argumentCaptor.getValue().getDetallesVenta().get(1).getTotal(), "Se ha intentado cambiar el valor total en el detalle de la venta");

        Assertions.assertEquals(0, argumentCaptor.getValue().getDetallesVenta().get(2).getId(), "Se ha intentado actualizar un detalle de venta diferente al incluido en la venta que generó el evento");
        Assertions.assertEquals(1, argumentCaptor.getValue().getDetallesVenta().get(2).getVentaId(), "Se ha intentado modificar la venta que referncia el detalle de la venta");
        Assertions.assertEquals(3, argumentCaptor.getValue().getDetallesVenta().get(2).getOrden(), "Se ha intentado cambiar el orden de la detalle de la venta");
        Assertions.assertEquals(PRODUCTO_SENCILLO_ID, argumentCaptor.getValue().getDetallesVenta().get(2).getProductoId(), "Se ha intentado el producto referenciado por el detalle de la venta");
        Assertions.assertEquals(kit.getCantidad(), argumentCaptor.getValue().getDetallesVenta().get(2).getCantidad(), "Se ha intentado la cantidad de productos vendidos en el detalle de la venta");
        Assertions.assertEquals(0, argumentCaptor.getValue().getDetallesVenta().get(2).getValorSinIva(), "Se ha intentado cambiar el valor sin iva en el detalle de la venta");
        Assertions.assertEquals(0, argumentCaptor.getValue().getDetallesVenta().get(2).getIva(), "Se ha intentado cambiar el iva en el detalle de la venta");
        Assertions.assertEquals(0, argumentCaptor.getValue().getDetallesVenta().get(2).getTotal(), "Se ha intentado cambiar el valor total en el detalle de la venta");
    }

    private DetalleVenta getDetalleVentaProductoSencillo(Venta venta) {
        DetalleVenta dv = new DetalleVenta();
        dv.setId(DETALLE_VENTA_2);
        dv.setVentaId(venta.getId());
        dv.setOrden(1);
        dv.setProductoId(PRODUCTO_SENCILLO_ID);
        dv.setCantidad(1);
        dv.setValorSinIva(100);
        dv.setIva(19);
        dv.setTotal(119);
        return dv;
    }

    private DetalleVenta getDetalleVentaProductoKit(Venta venta) {
        DetalleVenta dv = new DetalleVenta();
        dv.setId(DETALLE_VENTA_4);
        dv.setVentaId(venta.getId());
        dv.setOrden(2);
        dv.setProductoId(PRODUCTO_KIT_ID);
        dv.setCantidad(1);
        dv.setValorSinIva(1000);
        dv.setIva(190);
        dv.setTotal(1190);
        return dv;
    }

    private Venta getVenta() {
        Venta venta = new Venta();
        venta.setId(1);
        venta.setTarjeta(0);
        venta.setCreado(new Date());
        return venta;
    }

    private void setTotalesVenta(Venta venta) {
        venta.setValorSinIva(0);
        venta.setIva(0);
        venta.setTotal(0);
        venta.getDetallesVenta().stream().forEach(dv -> {
            venta.setValorSinIva(venta.getValorSinIva() + dv.getValorSinIva());
            venta.setIva(venta.getIva() + dv.getIva());
            venta.setTotal(venta.getTotal() + dv.getTotal());
        });
        venta.setEfectivo(venta.getTotal());
    }

}