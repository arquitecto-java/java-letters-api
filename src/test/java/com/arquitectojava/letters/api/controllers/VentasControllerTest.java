package com.arquitectojava.letters.api.controllers;

import com.arquitectojava.letters.api.services.VentasService;
import com.arquitectojava.letters.api.domain.sql.Venta;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class VentasControllerTest {

    @MockBean
    VentasService ventasService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SimpleDateFormat sdf;

    /**
     * Valida:
     *
     * al registrar la venta se retorna la información de la misma, junto con el id generado.
     * se retorna la información del cliente.
     *
     * Requiere que {@link VentasService#save(Venta)} ejecute su lógica y no genere excepción
     *
     * @throws Exception
     */
    @Test
    @DisplayName("POST /sales - Success")
    public void test_create_sale() throws Exception {
        //Behavior
        String payload = "{\n" +
                "  \"client\": {\n" +
                "    \"id\": 20,\n" +
                "    \"fname\": \"Sin registro\",\n" +
                "    \"lname\": \"null\",\n" +
                "    \"doc_id\": \"null\",\n" +
                "    \"email\": \"null\",\n" +
                "    \"phone\": \"null\"\n" +
                "  },\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"order\": 1,\n" +
                "      \"product\": {\n" +
                "        \"id\": 213,\n" +
                "        \"name\": \"PLUMON PARCHESITO X 6\",\n" +
                "        \"price\": 4700,\n" +
                "        \"tax\": 19\n" +
                "      },\n" +
                "      \"quantity\": 1\n" +
                "    }\n" +
                "  ],\n" +
                "  \"total\": {\n" +
                "    \"subtotal\": 3950,\n" +
                "    \"iva\": 750,\n" +
                "    \"total\": 4700\n" +
                "  },\n" +
                "  \"payment\": {\n" +
                "    \"cash\": 0,\n" +
                "    \"card\": \"4700\",\n" +
                "    \"bank\": \"0\",\n" +
                "    \"debt\": 0\n" +
                "  },\n" +
                "  \"created\": \"2020-04-11T18:23:10.272Z\"\n" +
                "}";

        when(ventasService.save(any()))
                .thenAnswer(i -> {
                    Venta v = i.getArgument(0);
                    v.setId(123);

                    v.getCliente().setFname("Andres");
                    v.getCliente().setLname("Jimenez");

                    return v;
                });

        // SUT and Assert
        mockMvc
                .perform(
                        post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //.andExpect(content().encoding("UTF-8"))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients/1"))

                .andExpect(jsonPath("$.id").value(123))
                .andExpect(jsonPath("$.client.id").value(20))
                .andExpect(jsonPath("$.client.fname").value("Andres"))
                .andExpect(jsonPath("$.client.lname").value("Jimenez"))
                .andExpect(jsonPath("$.items[0].order").value(1))
                .andExpect(jsonPath("$.items[0].product.id").value(213))
                .andExpect(jsonPath("$.items[0].product.name").value("PLUMON PARCHESITO X 6"))
                .andExpect(jsonPath("$.items[0].product.price").value(4700))//TODO: Validar en test de servicio no haga update de precio de producto.
                .andExpect(jsonPath("$.items[0].product.tax").value(19))//TODO: Revisar en test de servicio no sea vulnerabilidad de seguridad
                .andExpect(jsonPath("$.items[0].quantity").value(1))
                .andExpect(jsonPath("$.total.subtotal").value(3950))
                .andExpect(jsonPath("$.total.iva").value(750))
                .andExpect(jsonPath("$.total.total").value(4700))
                .andExpect(jsonPath("$.payment.cash").value(0))
                .andExpect(jsonPath("$.payment.card").value(4700))
                //.andExpect(jsonPath("$.payment.bank").value(0))
                .andExpect(jsonPath("$.payment.debt").value(0))
                .andExpect(jsonPath("$.created").value("2020-04-11T18:23:10.272Z"));

        //Verify
        verify(ventasService, times(1)).save(any());
    }

    //TODO: implement
    @Test
    @DisplayName("POST /sales - Client not exists")
    public void test_create_sale_client_not_exists() throws Exception { }

    //TODO: implement
    @Test
    @DisplayName("POST /sales - Busniess exception")
    public void test_create_sale_business_exception() throws Exception { }
}