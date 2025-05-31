package ro.unibuc.fmi.awbd.fixtures;

import lombok.val;
import ro.unibuc.fmi.awbd.controller.models.ClientCreationDto;
import ro.unibuc.fmi.awbd.controller.models.ClientDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.ClientUpdateDto;
import ro.unibuc.fmi.awbd.domain.user.model.Role;
import ro.unibuc.fmi.awbd.domain.user.model.client.Client;

public class ClientFixtures {
    private ClientFixtures() {
    }

    public static ClientCreationDto getClientCreationDtoFixture() {
        return new ClientCreationDto("client@new.com", "password", "New Client", "0712345678");
    }

    public static ClientUpdateDto getClientUpdateDtoFixture() {
        return new ClientUpdateDto("new password", "New Client Name", "0787654321");
    }

    public static Client getClientFixture() {
        val client = new Client();
        client.setId(1L);
        client.setEmail("client@client.com");
        client.setPassword("password");
        client.setName("Client Name");
        client.setRole(Role.CLIENT);
        client.setPhoneNumber("0712345678");
        return client;
    }

    public static ClientDetailsDto getClientDetailsDtoFixture() {
        val clientDetails = new ClientDetailsDto();
        clientDetails.setId(1L);
        clientDetails.setEmail("client@client.com");
        clientDetails.setName("Client Name");
        clientDetails.setPhoneNumber("0712345678");
        return clientDetails;
    }
}
