package com.policytracker.client;

import com.policytracker.common.events.ClientRegisteredEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ClientService clientService;

    @Test
    void registerClientSavesClientInRepository() {
        Client persistedClient = new Client();
        persistedClient.setId(1L);
        persistedClient.setFirstName("Jan");
        persistedClient.setLastName("Kowalski");
        persistedClient.setEmail("jan.kowalski@example.com");
        persistedClient.setPesel("90010112345");

        when(clientRepository.save(any(Client.class))).thenReturn(persistedClient);

        clientService.registerClient("Jan", "Kowalski", "jan.kowalski@example.com", "90010112345");

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository, times(1)).save(captor.capture());
        Client savedClient = captor.getValue();

        assertThat(savedClient.getFirstName()).isEqualTo("Jan");
        assertThat(savedClient.getLastName()).isEqualTo("Kowalski");
        assertThat(savedClient.getEmail()).isEqualTo("jan.kowalski@example.com");
        assertThat(savedClient.getPesel()).isEqualTo("90010112345");
    }

    @Test
    void registerClientPublishesClientRegisteredEvent() {
        Client persistedClient = new Client();
        persistedClient.setId(1L);
        persistedClient.setFirstName("Jan");
        persistedClient.setLastName("Kowalski");
        persistedClient.setEmail("jan.kowalski@example.com");
        persistedClient.setPesel("90010112345");

        when(clientRepository.save(any(Client.class))).thenReturn(persistedClient);

        clientService.registerClient("Jan", "Kowalski", "jan.kowalski@example.com", "90010112345");

        ArgumentCaptor<ClientRegisteredEvent> eventCaptor = ArgumentCaptor.forClass(ClientRegisteredEvent.class);
        verify(eventPublisher, times(1)).publishEvent(eventCaptor.capture());
        ClientRegisteredEvent event = eventCaptor.getValue();

        assertThat(event.clientId()).isEqualTo(1L);
    }
}
