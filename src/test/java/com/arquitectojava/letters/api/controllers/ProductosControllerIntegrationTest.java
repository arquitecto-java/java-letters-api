package com.arquitectojava.letters.api.controllers;

import com.arquitectojava.letters.api.repositories.ProductosRepository;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
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
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductosControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ProductosRepository productosRepository;

    @Autowired
    private SimpleDateFormat sdf;

    private Integer idOffering;
    private long testStartTimeMillis;

    private static boolean initialized;

    @BeforeAll
    public void beforeAll() throws Exception {
        if (ProductosControllerIntegrationTest.this == null || initialized) return;
        while(idOffering == null || productosRepository.existsById(idOffering)){
            idOffering = (int) (Math.random() * 10000);
        }
        initialized = true;
    }

    @AfterAll
    public void afterAll() throws Exception {
        productosRepository.deleteById(productosRepository.lettersProductBySku(idOffering + "").getId());
    }

    @Test
    @DisplayName("Create product with all information")
    @Order(1)
    public void test_create_offering() throws Exception {
        //Behavior
        String payload = "{\n" +
                "   \"name\":\"BISTURI GRANDE SENCILLO\",\n" +
                "   \"stock\":14,\n" +
                "   \"tax\":19,\n" +
                "   \"price\":1000,\n" +
                "   \"image\":\"http://www.google.com\",\n" +
                "   \"package_quantity\":1,\n" +
                "   \"category\":\"bisturís\"\n" +
                "}";

        testStartTimeMillis = System.currentTimeMillis();

        // SUT and Assert
        ResultActions resultActions = mockMvc
                .perform(
                        post("/offerings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients"))

                .andExpect(jsonPath("$.name").value("BISTURI GRANDE SENCILLO"))
                .andExpect(jsonPath("$.stock").value(14))
                .andExpect(jsonPath("$.tax").value(19))
                .andExpect(jsonPath("$.price").value(1000))
                .andExpect(jsonPath("$.image").value("http://www.google.com"))
                .andExpect(jsonPath("$.package_quantity").value(1))
                .andExpect(jsonPath("$.category").value("bisturís"));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString(Charset.defaultCharset());
        DocumentContext Document = JsonPath.parse(contentAsString);
        idOffering = Document.read("$.id");
        Assertions.assertNotNull(Document.read("$.created"));
        Date created = sdf.parse(Document.read("$.created"));
        Assertions.assertNotNull(Document.read("$.updated"));
        Date updated = sdf.parse(Document.read("$.updated"));
        Assertions.assertTrue(testStartTimeMillis < created.getTime());
        Assertions.assertTrue(testStartTimeMillis < updated.getTime());
    }

    @Test
    @DisplayName("Update product image")
    @Order(2)
    public void test_update_offering_image() throws Exception {
        //Behavior
        String payload = "{\n" +
                "   \"id\":" + idOffering + "\n" +
                "   \"image\":\"http://www.google.com.co\"\n" +
                "}";

        // SUT and Assert
        ResultActions resultActions = mockMvc
                .perform(
                        put("/offering/images/" + idOffering)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients"))

                .andExpect(jsonPath("$.name").value("BISTURI GRANDE SENCILLO"))
                .andExpect(jsonPath("$.stock").value(14))
                .andExpect(jsonPath("$.tax").value(19))
                .andExpect(jsonPath("$.price").value(1000))
                .andExpect(jsonPath("$.image").value("http://www.google.com.co"))
                .andExpect(jsonPath("$.package_quantity").value(1))
                .andExpect(jsonPath("$.category").value("bisturís"));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString(Charset.defaultCharset());
        DocumentContext Document = JsonPath.parse(contentAsString);
        idOffering = Document.read("$.id");
        Assertions.assertNotNull(Document.read("$.created"));
        Date created = sdf.parse(Document.read("$.created"));
        Assertions.assertNotNull(Document.read("$.updated"));
        Date updated = sdf.parse(Document.read("$.updated"));
        Assertions.assertTrue(testStartTimeMillis < created.getTime());
        Assertions.assertTrue(testStartTimeMillis < updated.getTime());
    }

    @Test
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

                .andExpect(jsonPath("$.[?(@.id==" + idOffering + ")].id").value(idOffering))
                .andExpect(jsonPath("$.[?(@.id==" + idOffering + ")].name").value("BISTURI GRANDE SENCILLO"))
                .andExpect(jsonPath("$.[?(@.id==" + idOffering + ")].stock").value(14))
                .andExpect(jsonPath("$.[?(@.id==" + idOffering + ")].tax").value(19))
                .andExpect(jsonPath("$.[?(@.id==" + idOffering + ")].price").value(1000))
                .andExpect(jsonPath("$.[?(@.id==" + idOffering + ")].image").value("http://www.google.com"))
                .andExpect(jsonPath("$.[?(@.id==" + idOffering + ")].package_quantity").value(1))
                .andExpect(jsonPath("$.[?(@.id==" + idOffering + ")].category").value("bisturís"));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString(Charset.defaultCharset());
        DocumentContext jsonDocument = JsonPath.parse(contentAsString);
        Assertions.assertNotNull(jsonDocument.read("$.[?(@.id==" + idOffering + ")].created"));
        Date created = sdf.parse(jsonDocument.read("$.[?(@.id==" + idOffering + ")].created", JSONArray.class).get(0).toString());
        Assertions.assertNotNull(jsonDocument.read("$.[?(@.id==" + idOffering + ")].updated"));
        Date updated = sdf.parse(jsonDocument.read("$.[?(@.id==" + idOffering + ")].updated", JSONArray.class).get(0).toString());
        Assertions.assertTrue(testStartTimeMillis < created.getTime());
        Assertions.assertTrue(testStartTimeMillis < updated.getTime());
    }

}