package com.arquitectojava.letters.api.clientes.controllers;

import com.arquitectojava.letters.api.LettersServicesApplication;
import com.arquitectojava.letters.api.clientes.repositories.ClientesRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LettersServicesApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientesRepository clientesRepository;

    @Autowired
    private SimpleDateFormat sdf;

    private Integer idClient;
    private long testStartTimeMillis;

    @AfterAll
    public void afterAll() throws Exception {
        if (idClient != null) clientesRepository.deleteById(idClient);
    }

    @Test
    @DisplayName("Create client failed")
    @Order(1)
    public void test_create_client_failed() throws Exception {
        //Behavior
        String payload = "{\n" +
                "   \"fname\":\"Andrés\"\n" +
                "}";

        // SUT and Assert
        ResultActions resultActions = mockMvc
                .perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isPreconditionFailed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients/1"))

                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("Create duplicated client failed")
    @Order(2)
    public void test_create_duplicated_client_failed() throws Exception {
        //Behavior
        String payload = "{\n" +
                "   \"fname\":\"Andrés\",\n" +
                "   \"doc_id\":\"80818313\"\n" +
                "}";

        // SUT and Assert
        ResultActions resultActions = mockMvc
                .perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isPreconditionFailed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients/1"))

                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.existing_clients").isArray());
    }

    @Test
    @DisplayName("Create client with all information")
    @Order(3)
    public void test_create_client() throws Exception {
        //Behavior
        String payload = "{\n" +
                "   \"fname\":\"Andrés\",\n" +
                "   \"lname\":\"Jiménez\",\n" +
                "   \"doc_id\":\"123456789\",\n" +
                "   \"phone\":\"3012345678\",\n" +
                "   \"instagram\":\"adrz1co_temp\",\n" +
                "   \"email\":\"adrz1_temp@hotmail.com\",\n" +
                "   \"address\":\"calle 57 # 10 - 60, casa 46\"\n" +
                "}";

        testStartTimeMillis = System.currentTimeMillis();

        // SUT and Assert
        ResultActions resultActions = mockMvc
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

                .andExpect(jsonPath("$.fname").value("Andrés"))
                .andExpect(jsonPath("$.lname").value("Jiménez"))
                .andExpect(jsonPath("$.doc_id").value("123456789"))
                .andExpect(jsonPath("$.email").value("adrz1_temp@hotmail.com"))
                .andExpect(jsonPath("$.phone").value("3012345678"))
                .andExpect(jsonPath("$.instagram").value("adrz1co_temp"))
                .andExpect(jsonPath("$.address").value("calle 57 # 10 - 60, casa 46"));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString(Charset.defaultCharset());
        DocumentContext Document = JsonPath.parse(contentAsString);
        idClient = Document.read("$.id");
        Assertions.assertNotNull(idClient);
        Assertions.assertNotNull(Document.read("$.created"));
        Date created = sdf.parse(Document.read("$.created"));
        Assertions.assertTrue(testStartTimeMillis < created.getTime());
    }

    @Test
    @DisplayName("List clients contains previously created")
    @Order(4)
    public void test_list_clients_contains_previously_created() throws Exception {
        do {
            Thread.sleep(200);
        } while (testStartTimeMillis == 0);

        // SUT and Assert
        ResultActions resultActions = mockMvc
                .perform(
                        get("/clients")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                //.andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                //.andExpect(header().string(HttpHeaders.LOCATION, "/clients"))

                .andExpect(jsonPath("$.[?(@.id==" + idClient + ")].id").value(idClient))
                .andExpect(jsonPath("$.[?(@.id==" + idClient + ")].fname").value("Andrés"))
                .andExpect(jsonPath("$.[?(@.id==" + idClient + ")].lname").value("Jiménez"))
                .andExpect(jsonPath("$.[?(@.id==" + idClient + ")].doc_id").value("123456789"))
                .andExpect(jsonPath("$.[?(@.id==" + idClient + ")].phone").value("3012345678"))
                .andExpect(jsonPath("$.[?(@.id==" + idClient + ")].instagram").value("adrz1co_temp"))
                .andExpect(jsonPath("$.[?(@.id==" + idClient + ")].email").value("adrz1_temp@hotmail.com"))
                .andExpect(jsonPath("$.[?(@.id==" + idClient + ")].address").value("calle 57 # 10 - 60, casa 46"));

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString(Charset.defaultCharset());
        DocumentContext jsonDocument = JsonPath.parse(contentAsString);
        Assertions.assertNotNull(jsonDocument.read("$.[?(@.id==" + idClient + ")].created"));
        Date created = sdf.parse(jsonDocument.read("$.[?(@.id==" + idClient + ")].created", JSONArray.class).get(0).toString());
        Assertions.assertTrue(testStartTimeMillis < created.getTime());
    }
}