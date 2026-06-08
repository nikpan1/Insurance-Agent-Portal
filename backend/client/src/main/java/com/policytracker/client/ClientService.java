package com.policytracker.client;

import com.policytracker.common.events.ClientRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Client registerClient(String firstName, String lastName, String email, String pesel) {
        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setPesel(pesel);

        Client savedClient = clientRepository.save(client);
        eventPublisher.publishEvent(ClientRegisteredEvent.builder()
                .clientId(savedClient.getId())
                .build());
        return savedClient;
    }
}
