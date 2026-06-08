package com.policytracker.client.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.policytracker.client.Client;
import com.policytracker.client.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientService clientService;

    @Test
    void registerClientReturnsCreatedForValidPayload() throws Exception {
        Client createdClient = new Client();
        createdClient.setId(100L);

        when(clientService.registerClient(anyString(), anyString(), anyString(), anyString())).thenReturn(createdClient);

        ClientRegistrationRequestDTO request = new ClientRegistrationRequestDTO(
                "Jan",
                "Kowalski",
                "jan.kowalski@example.com",
                "90010112345"
        );

        mockMvc.perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/clients/100"));
    }

    @Test
    void registerClientReturnsBadRequestForInvalidPayload() throws Exception {
        ClientRegistrationRequestDTO request = new ClientRegistrationRequestDTO(
                "Jan",
                "Kowalski",
                "not-an-email",
                "123"
        );

        mockMvc.perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(clientService, never()).registerClient(anyString(), anyString(), anyString(), anyString());
    }
}
