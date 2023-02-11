package com.arquitectojava.letters.api.clientes.controllers;

import com.arquitectojava.letters.api.clientes.domain.sql.Cliente;
import com.arquitectojava.letters.api.clientes.services.ClientesService;
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

import java.util.Collections;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ClientesControllerTest {

    @MockBean
    ClientesService clientesService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /clientes")
    public void test_list_clients() throws Exception {
        //Behavior
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
        when(clientesService.findAll())
                .thenReturn(Collections.singletonList(cliente));

        // SUT and Assert
        mockMvc
                .perform(
                        get("/clientes")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clientes"))

                .andExpect(jsonPath("$[0].id").value(cliente.getId()))
                .andExpect(jsonPath("$[0].fname").value(cliente.getFname()))
                .andExpect(jsonPath("$[0].lname").value(cliente.getLname()))
                .andExpect(jsonPath("$[0].doc_id").value(cliente.getDocId()))
                .andExpect(jsonPath("$[0].phone").value(cliente.getPhone()))
                .andExpect(jsonPath("$[0].instagram").value(cliente.getInstagram()))
                .andExpect(jsonPath("$[0].email").value(cliente.getEmail()))
                .andExpect(jsonPath("$[0].address").value(cliente.getAddress()));

        //Verify
        verify(clientesService, atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("POST /clientes - Success")
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
                        post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clientes/1"))

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

}