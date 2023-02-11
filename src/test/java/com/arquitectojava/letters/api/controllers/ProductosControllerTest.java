package com.arquitectojava.letters.api.controllers;

import com.arquitectojava.letters.api.services.ProductosService;
import com.arquitectojava.letters.api.domain.sql.Producto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ProductosControllerTest {

    @MockBean
    ProductosService productosService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SimpleDateFormat sdf;

    /**
     * Valida para productos sencillos:
     *
     * se haga bien el mapeo entre entidades internas (hibernate) y entidades json y esta relación sea uno a uno.
     * y luego de entidades json a su representación json.
     *
     * Requiere que {@link ProductosService#findAll()} retorne un listado con al menos un producto sencillo
     *
     * @throws Exception
     */
    @Test
    @DisplayName("GET /offerings")
    public void test_list_offerings() throws Exception {
        //Behavior
        Producto producto = new Producto();
        producto.setId(1);
        producto.setSku("1");
        producto.setNombre("BISTURI GRANDE SENCILLO");
        producto.setInventario(14);
        producto.setIva(19);
        producto.setPrecio(1000);
        producto.setImagen("http://www.google.com");
        producto.setCantidadPorEmpaque(1);
        producto.setCategoria("bisturís");
        producto.setCreado(sdf.parse("2020-02-26T04:55:28.000Z"));
        producto.setActualizado(sdf.parse("2020-03-18T19:20:53.000Z"));
        when(productosService.findAll())
                .thenReturn(Collections.singletonList(producto));

        // SUT and Assert
        mockMvc
                .perform(
                        get("/offerings")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients"))

                .andExpect(jsonPath("$[0].id").value(producto.getSku()))
                .andExpect(jsonPath("$[0].name").value(producto.getNombre()))
                .andExpect(jsonPath("$[0].stock").value(producto.getInventario()))
                .andExpect(jsonPath("$[0].tax").value(producto.getIva()))
                .andExpect(jsonPath("$[0].price").value(producto.getPrecio()))
                .andExpect(jsonPath("$[0].image").value(producto.getImagen()))
                .andExpect(jsonPath("$[0].package_quantity").value(producto.getCantidadPorEmpaque()))
                .andExpect(jsonPath("$[0].category").value(producto.getCategoria()))
                .andExpect(jsonPath("$[0].created").value(sdf.format(producto.getCreado())))
                .andExpect(jsonPath("$[0].updated").value(sdf.format(producto.getActualizado())));

        //Verify
        verify(productosService, atLeastOnce()).findAll();
    }

    /**
     * Valida que en caso de haber multiproductos:
     *
     * se tenga una entidad persistente representando el "multiproducto" adicional a una entidad persistente por cada variante.
     * se entregue una sola entidad json (multiproducto) con la información de las variantes.
     * se haga bien el mapeo entre entidades internas (hibernate) y entidades json
     * y luego de entidades json a su representación json.
     *
     * Requiere que {@link ProductosService#findAll()} retorne un listado donde obntenga al menos un multiprod con variantes
     *
     * @throws Exception
     */
    @Test
    @DisplayName("GET /offerings multiprod")
    public void test_list_offerings_multiprod() throws Exception {
        //Behavior
        Producto multiProduct = new Producto();
        multiProduct.setId(1);
        multiProduct.setSku("135");
        multiProduct.setNombre("MARCADOR DORICOLOR GRAFICO INDIVIDUAL");
        multiProduct.setInventario(1);
        multiProduct.setPrecio(2500);
        multiProduct.setCantidadPorEmpaque(1);
        multiProduct.setIsParent(1);
        multiProduct.setCategoria("bisturís");
        multiProduct.setCreado(sdf.parse("2020-02-26T04:55:28.000Z"));
        multiProduct.setActualizado(sdf.parse("2020-03-18T19:20:53.000Z"));

        Producto productVariant1 = new Producto();
        productVariant1.setId(2);
        productVariant1.setSku("136");
        productVariant1.setNombre("MARCADOR DORICOLOR GRAFICO BLANCO");
        productVariant1.setInventario(10);
        productVariant1.setPrecio(2500);
        productVariant1.setCantidadPorEmpaque(1);
        productVariant1.setIsParent(-1);
        productVariant1.setCategoria("bisturís");
        productVariant1.setColor("FFFFFF");
        productVariant1.setCreado(sdf.parse("2020-02-26T04:55:28.000Z"));
        productVariant1.setActualizado(sdf.parse("2020-03-18T19:20:53.000Z"));

        Producto productVariant2 = new Producto();
        productVariant2.setId(3);
        productVariant2.setSku("137");
        productVariant2.setNombre("MARCADOR DORICOLOR GRAFICO ROJO");
        productVariant2.setInventario(5);
        productVariant2.setPrecio(2500);
        productVariant2.setCantidadPorEmpaque(1);
        productVariant2.setIsParent(-1);
        productVariant2.setCategoria("bisturís");
        productVariant2.setCategoria("FF0000");
        productVariant2.setCreado(sdf.parse("2020-02-26T04:55:28.000Z"));
        productVariant2.setActualizado(sdf.parse("2020-03-18T19:20:53.000Z"));

        List<Producto> variants = new ArrayList<>();
        variants.add(productVariant1);
        variants.add(productVariant2);
        multiProduct.setVariantes(variants);

        when(productosService.findAll())
                .thenReturn(Collections.singletonList(multiProduct));

        // SUT and Assert
        mockMvc
                .perform(
                        get("/offerings")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients"))

                .andExpect(jsonPath("$[0].id").value(multiProduct.getSku()))
                .andExpect(jsonPath("$[0].name").value(multiProduct.getNombre()))
                .andExpect(jsonPath("$[0].stock").value(multiProduct.getInventario()))
                .andExpect(jsonPath("$[0].tax").value(multiProduct.getIva()))
                .andExpect(jsonPath("$[0].price").value(multiProduct.getPrecio()))
                .andExpect(jsonPath("$[0].package_quantity").value(multiProduct.getCantidadPorEmpaque()))
                .andExpect(jsonPath("$[0].category").value(multiProduct.getCategoria()))
                .andExpect(jsonPath("$[0].created").value(sdf.format(multiProduct.getCreado())))
                .andExpect(jsonPath("$[0].updated").value(sdf.format(multiProduct.getActualizado())))

                .andExpect(jsonPath("$[0].variants[0].id").value(productVariant1.getSku()))
                .andExpect(jsonPath("$[0].variants[0].name").value(productVariant1.getNombre()))
                .andExpect(jsonPath("$[0].variants[0].stock").value(productVariant1.getInventario()))
                .andExpect(jsonPath("$[0].variants[0].tax").value(productVariant1.getIva()))
                .andExpect(jsonPath("$[0].variants[0].price").value(productVariant1.getPrecio()))
                .andExpect(jsonPath("$[0].variants[0].package_quantity").value(productVariant1.getCantidadPorEmpaque()))
                .andExpect(jsonPath("$[0].variants[0].category").value(productVariant1.getCategoria()))
                .andExpect(jsonPath("$[0].variants[0].color").value(productVariant1.getColor()))
                .andExpect(jsonPath("$[0].variants[0].created").value(sdf.format(productVariant1.getCreado())))
                .andExpect(jsonPath("$[0].variants[0].updated").value(sdf.format(productVariant1.getActualizado())))

                .andExpect(jsonPath("$[0].variants[1].id").value(productVariant2.getSku()))
                .andExpect(jsonPath("$[0].variants[1].name").value(productVariant2.getNombre()))
                .andExpect(jsonPath("$[0].variants[1].stock").value(productVariant2.getInventario()))
                .andExpect(jsonPath("$[0].variants[1].tax").value(productVariant2.getIva()))
                .andExpect(jsonPath("$[0].variants[1].price").value(productVariant2.getPrecio()))
                .andExpect(jsonPath("$[0].variants[1].package_quantity").value(productVariant2.getCantidadPorEmpaque()))
                .andExpect(jsonPath("$[0].variants[1].category").value(productVariant2.getCategoria()))
                .andExpect(jsonPath("$[0].variants[1].color").value(productVariant2.getColor()))
                .andExpect(jsonPath("$[0].variants[1].created").value(sdf.format(productVariant2.getCreado())))
                .andExpect(jsonPath("$[0].variants[1].updated").value(sdf.format(productVariant2.getActualizado())));

        //Verify
        verify(productosService, times(1)).findAll();
    }

    //TODO: separar en otro test, este no es del pos sino de la administración.
    @Test
    @DisplayName("POST /offerings - Success")
    public void test_create_offering() throws Exception {
        //Behavior
        String payload = "{\n" +
                "   \"id\":1,\n" +
                "   \"name\":\"BISTURI GRANDE SENCILLO\",\n" +
                "   \"stock\":14,\n" +
                "   \"tax\":19,\n" +
                "   \"price\":1000,\n" +
                "   \"image\":\"http://www.google.com\",\n" +
                "   \"package_quantity\":1,\n" +
                "   \"category\":\"bistur\",\n" +
                "   \"created\":\"2020-02-26T04:55:28.000Z\",\n" +
                "   \"updated\":\"2020-03-18T19:20:53.000Z\"\n" +
                "}";

        Producto producto = new Producto();
        producto.setId(1);
        producto.setNombre("BISTURI GRANDE SENCILLO");
        producto.setInventario(14);
        producto.setIva(19);
        producto.setPrecio(1000);
        producto.setImagen("http://www.google.com");
        producto.setCantidadPorEmpaque(1);
        producto.setCategoria("bisturís");
        producto.setCreado(sdf.parse("2020-02-26T04:55:28.000Z"));
        producto.setActualizado(sdf.parse("2020-03-18T19:20:53.000Z"));

        when(productosService.save(any()))
                .thenReturn(producto);

        // SUT and Assert
        mockMvc
                .perform(
                        post("/offerings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients/1"))

                .andExpect(jsonPath("$.id").value(producto.getSku()))
                .andExpect(jsonPath("$.name").value(producto.getNombre()))
                .andExpect(jsonPath("$.stock").value(producto.getInventario()))
                .andExpect(jsonPath("$.tax").value(producto.getIva()))
                .andExpect(jsonPath("$.price").value(producto.getPrecio()))
                .andExpect(jsonPath("$.image").value(producto.getImagen()))
                .andExpect(jsonPath("$.package_quantity").value(producto.getCantidadPorEmpaque()))
                .andExpect(jsonPath("$.category").value(producto.getCategoria()))
                .andExpect(jsonPath("$.created").value(sdf.format(producto.getCreado())))
                .andExpect(jsonPath("$.updated").value(sdf.format(producto.getActualizado())));

        //Verify
        verify(productosService, atLeastOnce()).save(any());
    }

    //TODO: separar en otro test, este no es del pos sino de la administración.
    @Test
    @DisplayName("POST /offering/{id}/image - Success")
    public void test_update_offering_image() throws Exception {
        //Behavior
        String payload = "{\n" +
                "   \"id\":1,\n" +
                "   \"image\":\"http://www.google.com\"\n" +
                "}";

        Producto producto = new Producto();
        producto.setId(1);
        producto.setNombre("BISTURI GRANDE SENCILLO");
        producto.setInventario(14);
        producto.setIva(19);
        producto.setPrecio(1000);
        producto.setImagen("http://www.google.com");
        producto.setCantidadPorEmpaque(1);
        producto.setCategoria("bisturís");
        producto.setCreado(sdf.parse("2020-02-26T04:55:28.000Z"));
        producto.setActualizado(sdf.parse("2020-03-18T19:20:53.000Z"));

        when(productosService.saveImage(any()))
                .thenReturn(producto);

        // SUT and Assert
        mockMvc
                .perform(
                        put("/offering/images/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients/1"))

                .andExpect(jsonPath("$.id").value(producto.getSku()))
                .andExpect(jsonPath("$.name").value(producto.getNombre()))
                .andExpect(jsonPath("$.stock").value(producto.getInventario()))
                .andExpect(jsonPath("$.tax").value(producto.getIva()))
                .andExpect(jsonPath("$.price").value(producto.getPrecio()))
                .andExpect(jsonPath("$.image").value(producto.getImagen()))
                .andExpect(jsonPath("$.package_quantity").value(producto.getCantidadPorEmpaque()))
                .andExpect(jsonPath("$.category").value(producto.getCategoria()))
                .andExpect(jsonPath("$.created").value(sdf.format(producto.getCreado())))
                .andExpect(jsonPath("$.updated").value(sdf.format(producto.getActualizado())));

        //Verify
        verify(productosService, atLeastOnce()).saveImage(any());
    }

}