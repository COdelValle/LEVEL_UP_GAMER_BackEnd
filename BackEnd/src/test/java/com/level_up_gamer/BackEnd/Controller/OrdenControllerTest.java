package com.level_up_gamer.BackEnd.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.level_up_gamer.BackEnd.Service.Orden.OrdenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class OrdenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdenService ordenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void obtenerTodas_returnsOk() throws Exception {
        when(ordenService.getOrdenes()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/ordenes"))
                .andExpect(status().isOk());
    }
}
