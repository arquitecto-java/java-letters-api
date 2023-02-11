package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.repositories.VentasRepository;
import com.arquitectojava.letters.api.domain.sql.DetalleVenta;
import com.arquitectojava.letters.api.domain.sql.Producto;
import com.arquitectojava.letters.api.domain.sql.Venta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VentasServiceImplTest {

    @InjectMocks
    VentasServiceImpl ventasService;

    @Mock
    private VentasRepository ventasRepository;

    @Mock
    private ProductosService productosService;

    @Mock
    private ApplicationEventPublisher publisher;

    /**
     * Valida que cuando se realiza una venta de un producto sencillo:
     *
     * Se localiza el producto por sku.
     * no se alteran los valores de la venta.
     * se entrega la entidad guardada
     * se llama una sola vez a {@link VentasRepository#save(Object)}
     *
     * Requiere que {@link VentasRepository#save(Object)} retorne la información creada en base de datos
     *
     * @throws Exception
     */
    @Test
    @DisplayName("Venta de producto sencillo")
    public void test_ventas_producto_sencillo() throws Exception {
        //Behavior
        Venta venta = getVenta();

        DetalleVenta dv = getDetalleVenta(venta);
        venta.setDetallesVenta(Arrays.asList(dv));
        setTotalesVenta(venta);

        Producto producto = new Producto();
        producto.setId(123);
        producto.setSku("1");
        producto.setPrecio(1000);
        when(productosService.findBySku(dv.getProducto().getSku())).thenReturn(producto);
        when(ventasRepository.save(venta)).thenAnswer(i -> {
            Venta v = i.getArgument(0);
            v.setId(1);
            v.getDetallesVenta().get(0).setId(1);
            return v;
        });

        // SUT and Assert
        Venta ventaCreada = ventasService.save(venta);

        Assertions.assertNotNull(ventaCreada, "No se ha entregado la venta.");
        Assertions.assertNotNull(ventaCreada.getId(), "No se ha entregado id de la venta.");
        Assertions.assertEquals(100, ventaCreada.getValorSinIva(), "Se ha intentado cambiar el valor sin iva de la venta");
        Assertions.assertEquals(19, ventaCreada.getIva(), "Se ha intentado cambiar el iva de la venta");
        Assertions.assertEquals(119, ventaCreada.getTotal(), "Se ha intentado cambiar el valor total de la venta");
        Assertions.assertEquals(119, ventaCreada.getEfectivo(), "Se ha intentado cambiar el valor pagado con efectivo de la venta");
        Assertions.assertEquals(0, ventaCreada.getTarjeta(), "Se ha intentado cambiar el valor pagado con tarjeta de la venta");

        Assertions.assertEquals(1, ventaCreada.getDetallesVenta().size(), "Se ha intentado modificar los detalles de la venta");
        Assertions.assertNotNull(ventaCreada.getDetallesVenta().get(0).getId(), "No se ha entregado id de detalle de venta.");
        Assertions.assertNotNull(ventaCreada.getDetallesVenta().get(0).getProductoId(), "Se ha intentado crear un detalle de venta sin producto asociado.");
        Assertions.assertEquals(1, ventaCreada.getDetallesVenta().get(0).getOrden(), "Se ha intentado cambiar el orden de la detalle de la venta");
        Assertions.assertEquals(1, ventaCreada.getDetallesVenta().get(0).getCantidad(), "Se ha intentado la cantidad de productos vendidos en el detalle de la venta");
        Assertions.assertEquals(100, ventaCreada.getDetallesVenta().get(0).getValorSinIva(), "Se ha intentado cambiar el valor sin iva en el detalle de la venta");
        Assertions.assertEquals(19, ventaCreada.getDetallesVenta().get(0).getIva(), "Se ha intentado cambiar el iva en el detalle de la venta");
        Assertions.assertEquals(119, ventaCreada.getDetallesVenta().get(0).getTotal(), "Se ha intentado cambiar el valor total en el detalle de la venta");

        //Verify
        verify(ventasRepository, times(1)).save(venta);
    }

    /**
     * Valida que cuando se realiza una venta de un producto variante:
     *
     * Se localiza el producto por sku.
     * Se registre un detalle de venta asociado al multiproducto pero con valor en 0
     * no se alteran los valores de la venta.
     * se entrega la entidad guardada
     * se llama una sola vez a {@link VentasRepository#save(Object)}
     *
     * Requiere que {@link VentasRepository#save(Object)} retorne la información creada en base de datos
     *
     * @throws Exception
     */
    @Test
    @DisplayName("Venta de producto variante")
    public void test_ventas_producto_variante() throws Exception {
        //Behavior
        Venta venta = getVenta();

        DetalleVenta dv = getDetalleVenta(venta);
        ArrayList detallesVenta = new ArrayList();
        detallesVenta.add(dv);
        venta.setDetallesVenta(detallesVenta);
        setTotalesVenta(venta);

        Producto multiProduct = new Producto();
        multiProduct.setId(1);
        multiProduct.setSku("135");
        multiProduct.setNombre("MARCADOR DORICOLOR GRAFICO INDIVIDUAL");
        multiProduct.setInventario(1);
        multiProduct.setPrecio(2500);
        multiProduct.setCantidadPorEmpaque(1);
        multiProduct.setCategoria("bisturís");

        Producto productVariant = new Producto();
        productVariant.setId(2);
        productVariant.setSku("136");
        productVariant.setNombre("MARCADOR DORICOLOR GRAFICO BLANCO");
        productVariant.setInventario(10);
        productVariant.setPrecio(2500);
        productVariant.setCantidadPorEmpaque(1);
        productVariant.setCategoria("bisturís");
        productVariant.setEntitledItem(multiProduct);

        when(productosService.findBySku(dv.getProducto().getSku())).thenReturn(productVariant);
        when(ventasRepository.save(venta)).thenAnswer(i -> {
            Venta v = i.getArgument(0);
            v.setId(1);
            v.getDetallesVenta().get(0).setId(1);
            v.getDetallesVenta().get(1).setId(2);
            return v;
        });

        // SUT and Assert
        Venta ventaCreada = ventasService.save(venta);

        Assertions.assertNotNull(ventaCreada, "No se ha entregado la venta.");
        Assertions.assertNotNull(ventaCreada.getId(), "No se ha entregado id de la venta.");
        Assertions.assertEquals(100, ventaCreada.getValorSinIva(), "Se ha intentado cambiar el valor sin iva de la venta");
        Assertions.assertEquals(19, ventaCreada.getIva(), "Se ha intentado cambiar el iva de la venta");
        Assertions.assertEquals(119, ventaCreada.getTotal(), "Se ha intentado cambiar el valor total de la venta");
        Assertions.assertEquals(119, ventaCreada.getEfectivo(), "Se ha intentado cambiar el valor pagado con efectivo de la venta");
        Assertions.assertEquals(0, ventaCreada.getTarjeta(), "Se ha intentado cambiar el valor pagado con tarjeta de la venta");

        Assertions.assertEquals(2, ventaCreada.getDetallesVenta().size(), "Se ha intentado modificar los detalles de la venta");
        Assertions.assertNotNull(ventaCreada.getDetallesVenta().get(0).getId(), "No se ha entregado id de detalle de venta.");
        Assertions.assertNotNull(ventaCreada.getDetallesVenta().get(0).getProductoId(), "Se ha intentado crear un detalle de venta sin producto asociado.");
        Assertions.assertEquals(1, ventaCreada.getDetallesVenta().get(0).getOrden(), "Se ha intentado cambiar el orden de la detalle de la venta");
        Assertions.assertEquals(1, ventaCreada.getDetallesVenta().get(0).getCantidad(), "Se ha intentado la cantidad de productos vendidos en el detalle de la venta");
        Assertions.assertEquals(100, ventaCreada.getDetallesVenta().get(0).getValorSinIva(), "Se ha intentado cambiar el valor sin iva en el detalle de la venta");
        Assertions.assertEquals(19, ventaCreada.getDetallesVenta().get(0).getIva(), "Se ha intentado cambiar el iva en el detalle de la venta");
        Assertions.assertEquals(119, ventaCreada.getDetallesVenta().get(0).getTotal(), "Se ha intentado cambiar el valor total en el detalle de la venta");

        Assertions.assertNotNull(ventaCreada.getDetallesVenta().get(1).getId(), "No se ha entregado id de detalle de venta.");
        Assertions.assertNotNull(ventaCreada.getDetallesVenta().get(1).getProductoId(), "Se ha intentado crear un detalle de venta sin producto asociado.");
        Assertions.assertNotNull(ventaCreada.getDetallesVenta().get(1).getDetalleVentaReal(), "No se asoció el detalle de venta real al detalle de venta creado para referenciar el multiproducto");
        Assertions.assertEquals(1, ventaCreada.getDetallesVenta().get(1).getOrden(), "Se ha intentado cambiar el orden de la detalle de la venta");
        Assertions.assertEquals(1, ventaCreada.getDetallesVenta().get(1).getCantidad(), "Se ha intentado la cantidad de productos vendidos en el detalle de la venta");
        Assertions.assertEquals(100, ventaCreada.getDetallesVenta().get(1).getValorSinIva(), "Se ha intentado cambiar el valor sin iva en el detalle de la venta");
        Assertions.assertEquals(19, ventaCreada.getDetallesVenta().get(1).getIva(), "Se ha intentado cambiar el iva en el detalle de la venta");
        Assertions.assertEquals(119, ventaCreada.getDetallesVenta().get(1).getTotal(), "Se ha intentado cambiar el valor total en el detalle de la venta");

        //Verify
        verify(ventasRepository, times(1)).save(venta);
    }

    //TODO: implement
    @Test
    @DisplayName("Cliente no existe")
    public void test_ventas_cliente_no_existe() throws Exception {

    }

    //TODO: implement
    @Test
    @DisplayName("Venta vacía")
    public void test_ventas_venta_vacia() throws Exception {

    }

    //TODO: implement
    @Test
    @DisplayName("Venta total diferente a suma de detalles")
    public void test_ventas_total_diferente_a_suma_de_detalles() throws Exception {

    }

    //TODO: implement
    @Test
    @DisplayName("Venta total a pagado")
    public void test_ventas_total_diferente_a_pagado() throws Exception {

    }

    private DetalleVenta getDetalleVenta(Venta venta) {
        DetalleVenta dv = new DetalleVenta();
        dv.setOrden(1);
        dv.setProducto(new Producto());
        dv.getProducto().setSku("136");
        dv.setCantidad(1);
        dv.setValorSinIva(100);
        dv.setIva(19);
        dv.setTotal(119);
        return dv;
    }

    protected Venta getVenta() {
        Venta venta = new Venta();
        venta.setTarjeta(0);
        venta.setCreado(new Date());
        return venta;
    }

    protected void setTotalesVenta(Venta venta) {
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