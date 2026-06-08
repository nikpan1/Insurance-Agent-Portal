package com.policytracker.client.rest;

import com.policytracker.client.Client;
import com.policytracker.client.ClientService;
import com.policytracker.client.dto.ClientRegistrationRequestDTO;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<Void> registerClient(@Valid @RequestBody ClientRegistrationRequestDTO request) {
        Client createdClient = clientService.registerClient(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.pesel()
        );

        return ResponseEntity.created(URI.create("/api/v1/clients/" + createdClient.getId())).build();
    }
}
