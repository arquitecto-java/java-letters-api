package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.exceptions.ProductoMercadoDuplicadoPorSKUException;
import com.arquitectojava.letters.api.exceptions.ProveedorNoValidoProductoMercadoException;
import com.arquitectojava.letters.api.repositories.ProductosRepository;
import com.arquitectojava.letters.api.domain.sql.Producto;
import com.arquitectojava.letters.api.events.ProductoConMatchesActualizadoEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductosTercerosServiceImplTest {

    @InjectMocks
    ProductosTercerosServiceImpl productosTercerosService;

    @Mock
    private ProductosRepository productosRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @Test
    @DisplayName("Intentar guardar producto con proveedor null")
    public void test_save_producto_proveedor_null() throws Exception {
        //Behavior
        Producto received = new Producto();
        received.setId(1);
        received.setNombre("BOLIGRAFO BIC CRISTAL");

        // SUT and Assert
        ProveedorNoValidoProductoMercadoException thrown = Assertions.assertThrows(
                ProveedorNoValidoProductoMercadoException.class,
                () -> productosTercerosService.save(received),
                "No se reportó proveedor inválido"
        );

        //Assert

        //Verify
        verify(productosRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Intentar guardar producto con proveedor inválido")
    public void test_save_producto_proveedor_invalido() throws Exception {
        //Behavior
        Producto received = new Producto();
        received.setProveedorId(0);
        received.setId(1);
        received.setNombre("BOLIGRAFO BIC CRISTAL");

        // SUT and Assert
        ProveedorNoValidoProductoMercadoException thrown = Assertions.assertThrows(
                ProveedorNoValidoProductoMercadoException.class,
                () -> productosTercerosService.save(received),
                "No se reportó proveedor inválido"
        );

        //Assert

        //Verify
        verify(productosRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Intentar guardar producto duplicado por sku")
    public void test_save_producto_duplicado_por_sku() throws Exception {
        //Behavior
        Producto received = new Producto();
        received.setProveedorId(1);
        received.setSku("abc");
        received.setId(1);
        received.setNombre("BOLIGRAFO BIC CRISTAL");

        Producto existente = new Producto();
        existente.setProveedorId(1);
        existente.setSku("abc");
        existente.setId(2);
        existente.setNombre("BOLIGRAFO BIC CRISTAL");

        when(productosRepository.marketProductBySku("abc", 1)).thenReturn(existente);

        // SUT and Assert
        ProductoMercadoDuplicadoPorSKUException thrown = Assertions.assertThrows(
                ProductoMercadoDuplicadoPorSKUException.class,
                () -> productosTercerosService.save(received),
                "No se reportó producto duplicado"
        );

        //Assert

        //Verify
        verify(productosRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Guardar producto nuevo")
    public void test_save_producto_nuevo() throws Exception {
        //Behavior
        Producto received = new Producto();
        received.setProveedorId(1);
        received.setSku("abc");
        received.setNombre("BOLIGRAFO BIC CRISTAL");

        // SUT and Assert
        Producto actualizado = productosTercerosService.save(received);

        //Assert

        //Verify
        verify(productosRepository, times(1))
                .marketProductBySku(received.getSku(), received.getProveedorId());
        verify(productosRepository, times(1)).save(received);
    }

    @Test
    @DisplayName("Guardar producto existente")
    public void test_save_producto_existente() throws Exception {
        //Behavior
        Producto received = new Producto();
        received.setProveedorId(1);
        received.setSku("abcd");
        received.setId(1);
        received.setNombre("BOLIGRAFO BIC CRISTAL");

        Producto existente = new Producto();
        existente.setProveedorId(1);
        existente.setSku("abc");
        existente.setId(1);
        existente.setNombre("BOLIGRAFO BIC CRISTAL");

        when(productosRepository.findById(received.getId())).thenReturn(Optional.of(existente));
        when(productosRepository.save(any())).thenAnswer(i -> {
            return i.getArgument(0);
        });

        // SUT and Assert
        Producto actualizado = productosTercerosService.save(received);

        //Assert
        Assertions.assertEquals(received.getSku(), actualizado.getSku(), "So se actualizó sku");
        Assertions.assertEquals(received.getProveedorId(), actualizado.getProveedorId(), "So se actualizó proveedor");

        //Verify
        verify(productosRepository, times(1))
                .marketProductBySku(received.getSku(), received.getProveedorId());
        verify(productosRepository, times(1)).save(received);
    }

    @Test
    @DisplayName("Guardar producto nuevo con matches")
    public void test_save_producto_nuevo_con_matches() throws Exception {
        //Behavior
        Producto received = new Producto();
        received.setProveedorId(1);
        received.setSku("abc");
        received.setNombre("BOLIGRAFO BIC CRISTAL");

        received.setReverseMatchesSkus(Arrays.asList("sku-123"));

        when(productosRepository.save(received)).thenAnswer(a -> {
            Producto p = a.getArgument(0);
            p.setId(1);
            return p;
        });

        // SUT and Assert
        Producto nuevo = productosTercerosService.save(received);

        //Assert

        //Verify
        verify(productosRepository, times(1))
                .marketProductBySku(received.getSku(), received.getProveedorId());
        verify(productosRepository, times(1)).save(received);
        ArgumentCaptor<ProductoConMatchesActualizadoEvent> argumentCaptor = ArgumentCaptor.forClass(ProductoConMatchesActualizadoEvent.class);
        verify(publisher, times(1)).publishEvent(argumentCaptor.capture());

        Assertions.assertNotNull(argumentCaptor.getValue().getProducto().getId(), "Se ha enviado producto sin id");
        Assertions.assertEquals(1, argumentCaptor.getValue().getProducto().getReverseMatchesSkus().size(), "No se ha enviado skus de matches");
    }

    @Test
    @DisplayName("Guardar producto existente con matches")
    public void test_save_producto_existente_con_matches() throws Exception {
        //Behavior
        Producto received = new Producto();
        received.setProveedorId(1);
        received.setSku("abcd");
        received.setId(1);
        received.setNombre("BOLIGRAFO BIC CRISTAL");

        received.setReverseMatchesSkus(Arrays.asList("sku-123"));

        Producto existente = new Producto();
        existente.setProveedorId(1);
        existente.setSku("abc");
        existente.setId(1);
        existente.setNombre("BOLIGRAFO BIC CRISTAL");

        when(productosRepository.findById(received.getId())).thenReturn(Optional.of(existente));
        when(productosRepository.save(any())).thenAnswer(i -> {
            return i.getArgument(0);
        });

        // SUT and Assert
        Producto actualizado = productosTercerosService.save(received);

        //Assert
        Assertions.assertEquals(received.getSku(), actualizado.getSku(), "No se actualizó sku");
        Assertions.assertEquals(received.getProveedorId(), actualizado.getProveedorId(), "No se actualizó proveedor");

        //Verify
        verify(productosRepository, times(1))
                .marketProductBySku(received.getSku(), received.getProveedorId());
        verify(productosRepository, times(1)).findById(received.getId());
        verify(productosRepository, times(1)).save(existente);
        ArgumentCaptor<ProductoConMatchesActualizadoEvent> argumentCaptor = ArgumentCaptor.forClass(ProductoConMatchesActualizadoEvent.class);
        verify(publisher, times(1)).publishEvent(argumentCaptor.capture());

        Assertions.assertNotNull(argumentCaptor.getValue().getProducto().getId(), "Se ha enviado producto sin id");
        Assertions.assertEquals(1, argumentCaptor.getValue().getProducto().getReverseMatchesSkus().size(), "No se ha enviado skus de matches");
    }

}