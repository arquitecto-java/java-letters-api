package com.arquitectojava.letters.api.controllers;

import com.arquitectojava.letters.api.services.MatchesService;
import com.arquitectojava.letters.api.domain.sql.Match;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class MatchesControllerTest {

    @MockBean
    MatchesService matchesService;

    @Autowired
    private MockMvc mockMvc;

    /**
     * Valida que para un producto se obtienen ids de sus matches en forma de array.
     *
     * Requiere que {@link MatchesService#matchesBySku(String)} ()} retorne un listado con al menos un match
     *
     * @throws Exception
     */
    @Test
    @DisplayName("GET /matches/{id}")
    public void test_list_matches() throws Exception {
        //Behavior
        Match m1 = new Match();
        m1.setProductoId(1);
        m1.setMatchId(2);
        Match m2 = new Match();
        m2.setProductoId(1);
        m2.setMatchId(3);
        Match m3 = new Match();
        m3.setProductoId(1);
        m3.setMatchId(4);
        List<Match> existingMatches = Arrays.asList(m1, m2, m3);

        when(matchesService.matchesBySku("1"))
                .thenReturn(existingMatches);

        // SUT and Assert
        mockMvc
                .perform(
                        get("/matches/1")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients"))

                .andExpect(jsonPath("$[0]").value(existingMatches.get(0).getMatchId()))
                .andExpect(jsonPath("$[1]").value(existingMatches.get(1).getMatchId()))
                .andExpect(jsonPath("$[2]").value(existingMatches.get(2).getMatchId()));

        //Verify
        verify(matchesService, atLeastOnce()).matchesBySku("1");
    }

    /**
     * Valida que para un producto se obtienen ids de sus matches inversos en forma de array.
     *
     * Requiere que {@link MatchesService#reverseMatchesByProductId(int)} ()} retorne un listado con al menos un match sencillo
     *
     * @throws Exception
     */
    @Test
    @DisplayName("GET /reverse_matches/{id}")
    public void test_list_reverseMatches() throws Exception {
        //Behavior
        Match m1 = new Match();
        m1.setProductoId(2);
        m1.setMatchId(1);
        m1.setProducto(buildProducto(2, "sku-2"));
        Match m2 = new Match();
        m2.setProductoId(3);
        m2.setMatchId(1);
        m2.setProducto(buildProducto(3, "sku-3"));
        Match m3 = new Match();
        m3.setProductoId(4);
        m3.setMatchId(1);
        m3.setProducto(buildProducto(4, "sku-4"));
        List<Match> existingMatches = Arrays.asList(m1, m2, m3);

        when(matchesService.reverseMatchesByProductId(1))
                .thenReturn(existingMatches);

        // SUT and Assert
        mockMvc
                .perform(
                        get("/reverse_matches/1")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients"))

                .andExpect(jsonPath("$[0]").value(existingMatches.get(0).getProducto().getSku()))
                .andExpect(jsonPath("$[1]").value(existingMatches.get(1).getProducto().getSku()))
                .andExpect(jsonPath("$[2]").value(existingMatches.get(2).getProducto().getSku()));

        //Verify
        verify(matchesService, atLeastOnce()).reverseMatchesByProductId(1);
    }

    private Producto buildProducto(int id, String sku) {
        Producto p = new Producto();
        p.setId(id);
        p.setSku(sku);
        return p;
    }

}