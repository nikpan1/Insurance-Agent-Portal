package com.policytracker.client;

import com.policytracker.common.events.ClientRegisteredEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ClientService(ClientRepository clientRepository, ApplicationEventPublisher eventPublisher) {
        this.clientRepository = clientRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Client registerClient(String firstName, String lastName, String email, String pesel) {
        Client client = new Client();
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setPesel(pesel);

        Client savedClient = clientRepository.save(client);
        eventPublisher.publishEvent(new ClientRegisteredEvent(savedClient.getId()));
        return savedClient;
    }
}
