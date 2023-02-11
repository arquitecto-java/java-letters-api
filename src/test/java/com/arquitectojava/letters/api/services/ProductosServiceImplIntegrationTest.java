package com.arquitectojava.letters.api.services;

import com.arquitectojava.letters.api.domain.sql.Producto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductosServiceImplIntegrationTest {

    @Autowired
    ProductosServiceImpl productosService;

    @Test
    @DisplayName("Obtener todos los productos")
    public void test_find_all() throws Exception {
        //Behavior

        // SUT and Assert
        List<Producto> all = productosService.findAll();

        //Assert
        Assertions.assertNotNull(all);
    }

    @Test
    @DisplayName("Obtener un producto por sku")
    public void test_find_by_sku() throws Exception {
        //Behavior

        // SUT and Assert
        Producto found1 = productosService.findBySku("19");
        Producto found2 = productosService.findBySku("674");

        //Assert
        Assertions.assertNotNull(found1);
        Assertions.assertNotNull(found2);
    }

}