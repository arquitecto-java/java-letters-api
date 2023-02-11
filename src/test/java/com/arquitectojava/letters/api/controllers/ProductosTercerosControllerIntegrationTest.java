package com.arquitectojava.letters.api.controllers;

import com.arquitectojava.letters.api.repositories.MatchesRepository;
import com.arquitectojava.letters.api.repositories.ProductosRepository;
import com.arquitectojava.letters.api.domain.sql.Match;
import com.arquitectojava.letters.api.domain.sql.Producto;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductosTercerosControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ProductosRepository productosRepository;

    @Autowired
    MatchesRepository matchesRepository;

    @Autowired
    private SimpleDateFormat sdf;

    private Integer productoId;
    private Producto lettersProducto;
    private long testStartTimeMillis;

    private static boolean initialized;

    @BeforeAll
    public void beforeAll() throws Exception {
        if (ProductosTercerosControllerIntegrationTest.this == null || initialized) return;
        String lettersProductoSku = null;
        while((lettersProducto = productosRepository.lettersProductBySku(lettersProductoSku)) == null){
            lettersProductoSku = (int) (Math.random() * 1000) + "";
        }
        initialized = true;
    }

    @AfterAll
    public void afterAll() throws Exception {
        matchesRepository.reverseMatches(productoId).forEach(rm -> matchesRepository.deleteById(rm.getId()));
        productosRepository.deleteById(productoId);
    }

    @Test
    @DisplayName("Create market product with all information")
    @Order(1)
    public void test_create_market_product() throws Exception {
        //Behavior
        //Se genera un id único por semana ya que semananlmente db-test se reinicia
        String sku = (System.currentTimeMillis() % (3600 * 24 * 7)) + "";

        String payload = "{\n" +
                "  \"supplier_id\": 1,\n" +
                "  \"sku\": \"" + sku + "\",\n" +
                "  \"name\": \"LAPIZ NORMA ROJO TRIANGULAR (534895)\",\n" +
                "  \"price\": 533,\n" +
                "  \"reverse_matches\": [" + lettersProducto.getSku() + "]\n" +
                "}";

        testStartTimeMillis = System.currentTimeMillis();

        // SUT and Assert
        ResultActions resultActions = mockMvc
                .perform(
                        post("/market")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients"))

                .andExpect(jsonPath("$.supplier_id").value("1"))
                .andExpect(jsonPath("$.sku").value(sku))
                .andExpect(jsonPath("$.name").value("LAPIZ NORMA ROJO TRIANGULAR (534895)"))
                .andExpect(jsonPath("$.price").value(533))
                .andExpect(jsonPath("$.reverse_matches[0]").value(lettersProducto.getSku()));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString(Charset.defaultCharset());
        DocumentContext Document = JsonPath.parse(contentAsString);
        productoId = Document.read("$.id");

        List<Match> reverseMatches = matchesRepository.reverseMatches(productoId);
        Assertions.assertEquals(1, reverseMatches.size(), "No se asoció el match enviado");
        Assertions.assertEquals(lettersProducto.getId(), reverseMatches.get(0).getProductoId(), "No se asoció el match enviado");
    }

    /*@Test
    @DisplayName("List offerings contains previously created")
    @Order(3)
    public void test_list_offerings_contains_previously_created() throws Exception {
        // SUT and Assert
        ResultActions resultActions = mockMvc
                .perform(
                        get("/offerings")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients"))

                .andExpect(jsonPath("$.[?(@.id==" + productoId + ")].id").value(productoId))
                .andExpect(jsonPath("$.[?(@.id==" + productoId + ")].name").value("BISTURI GRANDE SENCILLO"))
                .andExpect(jsonPath("$.[?(@.id==" + productoId + ")].stock").value(14))
                .andExpect(jsonPath("$.[?(@.id==" + productoId + ")].tax").value(19))
                .andExpect(jsonPath("$.[?(@.id==" + productoId + ")].price").value(1000))
                .andExpect(jsonPath("$.[?(@.id==" + productoId + ")].image").value("http://www.google.com"))
                .andExpect(jsonPath("$.[?(@.id==" + productoId + ")].package_quantity").value(1))
                .andExpect(jsonPath("$.[?(@.id==" + productoId + ")].category").value("bisturís"));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString(Charset.defaultCharset());
        DocumentContext jsonDocument = JsonPath.parse(contentAsString);
        Assertions.assertNotNull(jsonDocument.read("$.[?(@.id==" + productoId + ")].created"));
        Date created = sdf.parse(jsonDocument.read("$.[?(@.id==" + productoId + ")].created", JSONArray.class).get(0).toString());
        Assertions.assertNotNull(jsonDocument.read("$.[?(@.id==" + productoId + ")].updated"));
        Date updated = sdf.parse(jsonDocument.read("$.[?(@.id==" + productoId + ")].updated", JSONArray.class).get(0).toString());
        Assertions.assertTrue(testStartTimeMillis < created.getTime());
        Assertions.assertTrue(testStartTimeMillis < updated.getTime());
    }*/

}