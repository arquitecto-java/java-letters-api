package com.arquitectojava.letters.api.controllers;

import com.arquitectojava.letters.api.repositories.ProductosRepository;
import com.arquitectojava.letters.api.services.MatchesService;
import com.arquitectojava.letters.api.domain.sql.Producto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MatchesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ProductosRepository productosRepository;

    @Autowired
    MatchesService matchesService;

    private String skuOffering;
    private Integer idMatch;

    private static boolean initialized;

    @BeforeAll
    public void beforeAll() throws Exception {
        if (MatchesControllerIntegrationTest.this == null || initialized) return;
        List<Producto> lettersProducts = productosRepository.lettersProducts();
        Producto p = lettersProducts.get((int) (Math.random() * lettersProducts.size()));
        skuOffering = p.getSku();
        List<Producto> marketProducts = productosRepository.marketProducts();
        idMatch = marketProducts.get((int) (Math.random() * marketProducts.size())).getId();
        p.setMatchesIds(Arrays.asList(idMatch));
        matchesService.updateMatches(p);
        initialized = true;
    }

    @Test
    @DisplayName("Test matches are returned")
    @Order(1)
    public void test_matches() throws Exception {
        //Behavior
        List<Integer> existingMatches = Arrays.asList(idMatch);

        // SUT and Assert
        ResultActions resultActions = mockMvc
                .perform(
                        get("/matches/" + skuOffering)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients"))

                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value(existingMatches.get(0)));

    }

    @Test
    @DisplayName("Test reverse matches are returned")
    @Order(2)
    public void test_reverse_matches() throws Exception {
        //Behavior
        List<String> existingReverseMatches = Arrays.asList(skuOffering);

        // SUT and Assert
        ResultActions resultActions = mockMvc
                .perform(
                        get("/reverse_matches/" + idMatch)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients"))

                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value(existingReverseMatches.get(0)));

    }

}