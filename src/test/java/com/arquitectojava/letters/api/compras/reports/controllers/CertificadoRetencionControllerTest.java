package com.arquitectojava.letters.api.compras.reports.controllers;

import com.arquitectojava.letters.api.compras.domain.sql.Compra;
import com.arquitectojava.letters.api.compras.services.ComprasService;
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
import java.util.Collections;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class CertificadoRetencionControllerTest {

    @MockBean
    ComprasService comprasService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SimpleDateFormat sdf;

    @Test
    @DisplayName("GET /purchases")
    public void test_list_purchases() throws Exception {
        //Behavior
        Compra compra = new Compra();
        compra.setId(1);
        compra.setProveedorId(1);
        compra.setConceptos("1, 2, 3");
        compra.setDescripcion("Compra para test unitario");
        compra.setCreado(new Date());
        compra.setComprado(new Date());
        compra.setRecibido(new Date());
        compra.setCostoTransporte(100);
        compra.setPagado(2000);

        when(comprasService.findAll())
                .thenReturn(Collections.singletonList(compra));

        // SUT and Assert
        mockMvc
                .perform(
                        get("/purchases")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients"))

                .andExpect(jsonPath("$[0].id").value(compra.getId()))
                .andExpect(jsonPath("$[0].supplier.id").value(compra.getProveedorId()))
                .andExpect(jsonPath("$[0].concepts[0].id").value("1"))
                .andExpect(jsonPath("$[0].concepts[1].id").value("2"))
                .andExpect(jsonPath("$[0].concepts[2].id").value("3"))
                .andExpect(jsonPath("$[0].description").value(compra.getDescripcion()))
                .andExpect(jsonPath("$[0].created").value(sdf.format(compra.getCreado())))
                .andExpect(jsonPath("$[0].purchased").value(sdf.format(compra.getComprado())))
                .andExpect(jsonPath("$[0].received").value(sdf.format(compra.getRecibido())))
                .andExpect(jsonPath("$[0].transport_cost").value(compra.getCostoTransporte()))
                .andExpect(jsonPath("$[0].paid").value(compra.getPagado()));

        //Verify
        verify(comprasService, atLeastOnce()).findAll();
    }
/*
    @Test
    @DisplayName("POST /clients - Success")
    public void test_create_client() throws Exception {
        //Behavior
        String payload = "{\n" +
                "   \"id\":1,\n" +
                "   \"fname\":\"Andrés\",\n" +
                "   \"lname\":\"Jiménez\",\n" +
                "   \"doc_id\":\"80818313\",\n" +
                "   \"phone\":\"3017549852\",\n" +
                "   \"instagram\":\"adrz1co\",\n" +
                "   \"email\":\"adrz1@hotmail.com\",\n" +
                "   \"address\":\"calle 57 # 10 - 60, casa 46\"\n" +
                "}";

        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setFname("Andrés");
        cliente.setLname("Jiménez");
        cliente.setDocId("80818313");
        cliente.setPhone("3017549852");
        cliente.setInstagram("adrz1co");
        cliente.setEmail("adrz1@hatit.co");
        cliente.setAddress("calle 57 # 10 - 60, casa 46");
        cliente.setCreated(new Date());

        when(clientesService.save(any()))
                .thenReturn(cliente);

        // SUT and Assert
        mockMvc
                .perform(
                        post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients/1"))

                .andExpect(jsonPath("$.id").value(cliente.getId()))
                .andExpect(jsonPath("$.fname").value(cliente.getFname()))
                .andExpect(jsonPath("$.lname").value(cliente.getLname()))
                .andExpect(jsonPath("$.doc_id").value(cliente.getDocId()))
                .andExpect(jsonPath("$.phone").value(cliente.getPhone()))
                .andExpect(jsonPath("$.instagram").value(cliente.getInstagram()))
                .andExpect(jsonPath("$.email").value(cliente.getEmail()))
                .andExpect(jsonPath("$.address").value(cliente.getAddress()));

        //Verify
        verify(clientesService, atLeastOnce()).save(any());
    }
*/
}