package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.repositories.ProductosRepository;
import com.arquitectojava.letters.api.domain.sql.Producto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductosServiceImplTest {

    @InjectMocks
    ProductosServiceImpl productosService;

    @Mock
    private ProductosRepository productosRepository;

    @Test
    @DisplayName("Guardar multiproducto")
    public void test_save_multiproduct_same_variants() throws Exception {
        //Behavior
        Producto received = new Producto();
        received.setSku("1");
        received.setNombre("BOLIGRAFO BIC CRISTAL");
        received.setCantidadPorEmpaque(1);
        received.setCategoria("lapiceros");
        received.setVariantes(new ArrayList<>());
        received.setIsParent(1);

        Producto variant1 = buildVariant("11", "BOLIGRAFO BIC CRISTAL NEGRO", 900);
        variant1.setColor("000000");
        received.getVariantes().add(variant1);

        Producto variant2 = buildVariant("12", "BOLIGRAFO BIC CRISTAL AZUL", 900);
        variant2.setColor("0000FF");
        received.getVariantes().add(variant2);

        Producto variant3 = buildVariant("13", "BOLIGRAFO BIC CRISTAL ROJO", 900);
        variant2.setColor("FF0000");
        received.getVariantes().add(variant3);

        Producto existing = new Producto();
        existing.setId(100);
        existing.setSku("1");
        existing.setNombre("BOLIGRAFO BIC CRISTAL");
        existing.setCantidadPorEmpaque(1);
        existing.setCategoria("lapiceros");
        existing.setVariantes(new ArrayList<>());

        Producto existingVariant1 = buildVariant(variant1.getSku(), "BOLIGRAFO BIC CRISTAL NEGRO", 800);
        existingVariant1.setId(101);
        existing.getVariantes().add(existingVariant1);

        Producto existingVariant2 = buildVariant(variant2.getSku(), "BOLIGRAFO BIC CRISTAL AZUL", 1000);
        existingVariant2.setId(102);
        existing.getVariantes().add(existingVariant2);

        Producto existingVariant3 = buildVariant(variant3.getSku(), "BOLIGRAFO BIC CRISTAL ROJO", 1100);
        existingVariant3.setId(103);
        existing.getVariantes().add(existingVariant3);

        when(productosRepository.lettersProductBySku(received.getSku())).thenReturn(existing);

        when(productosRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // SUT and Assert
        Producto saved = productosService.save(received);

        //Assert
        Assertions.assertEquals(existing, saved, "No se actualizó producto existente.");
        Producto savedVariant1 = saved.getVariantes().stream().filter(p -> p.getSku().equals(existingVariant1.getSku())).findFirst().orElse(null);
        Assertions.assertNotNull(savedVariant1, "No se actualizó variant1.");
        Assertions.assertEquals(variant1.getPrecio(), savedVariant1.getPrecio(), "No se actualizó precio de variant1.");
        Assertions.assertEquals(variant1.getInventario(), savedVariant1.getInventario(), "No se actualizó inventario de variant1.");
        Assertions.assertEquals(variant1.getColor(), savedVariant1.getColor(), "No se actualizó color de variant1.");
        Producto savedVariant2 = saved.getVariantes().stream().filter(p -> p.getSku().equals(existingVariant2.getSku())).findFirst().orElse(null);
        Assertions.assertNotNull(savedVariant2, "No se actualizó variant2.");
        Assertions.assertEquals(variant2.getPrecio(), savedVariant2.getPrecio(), "No se actualizó precio de variant2.");
        Assertions.assertEquals(variant2.getInventario(), savedVariant2.getInventario(), "No se actualizó inventario de variant2.");
        Assertions.assertEquals(variant2.getColor(), savedVariant2.getColor(), "No se actualizó color de variant2.");
        Producto savedVariant3 = saved.getVariantes().stream().filter(p -> p.getSku().equals(existingVariant3.getSku())).findFirst().orElse(null);
        Assertions.assertNotNull(savedVariant3, "No se actualizó variant3.");
        Assertions.assertEquals(variant3.getPrecio(), savedVariant3.getPrecio(), "No se actualizó precio de variant3.");
        Assertions.assertEquals(variant3.getInventario(), savedVariant3.getInventario(), "No se actualizó inventario de variant3.");
        Assertions.assertEquals(variant3.getColor(), savedVariant3.getColor(), "No se actualizó color de variant3.");

        //Verify
        verify(productosRepository, times(1)).lettersProductBySku(received.getSku());
    }

    protected Producto buildVariant(String sku, String nombre, int precio){
        Producto variant = new Producto();
        variant.setSku(sku);
        variant.setNombre(nombre);
        variant.setPrecio(precio);
        variant.setIva(19);
        return variant;
    }

}