package ro.unibuc.fmi.awbd.service.client;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.unibuc.fmi.awbd.common.exception.BadRequestException;
import ro.unibuc.fmi.awbd.common.exception.ForbiddenException;
import ro.unibuc.fmi.awbd.domain.user.repository.UserRepository;
import ro.unibuc.fmi.awbd.domain.user.repository.client.ClientRepository;
import ro.unibuc.fmi.awbd.fixtures.ClientFixtures;
import ro.unibuc.fmi.awbd.service.client.mapper.ClientMapper;
import ro.unibuc.fmi.awbd.service.user.UserInformationService;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    @Mock
    ClientRepository clientRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ClientMapper clientMapper;

    @Mock
    UserInformationService userInformationService;

    @InjectMocks
    ClientService clientService;

    @Test
    void givenDifferentClient_whenGetClientDetails_thenForbiddenException() {
        val client = ClientFixtures.getClientFixture();
        client.setId(2L);

        Mockito.when(userInformationService.getCurrentUser()).thenReturn(client);

        val exc = Assertions.assertThrows(ForbiddenException.class, () -> clientService.getClientDetails(1L));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Client not allowed to view details of other users", exc.getMessage());
    }

    @Test
    void givenClient_whenGetClientDetails_thenGetClientDetails() {
        val client = ClientFixtures.getClientFixture();
        val clientDetailsDto = ClientFixtures.getClientDetailsDtoFixture();

        Mockito.when(userInformationService.getCurrentUser()).thenReturn(client);
        Mockito.when(clientMapper.mapToClientDetailsDto(client)).thenReturn(clientDetailsDto);

        Assertions.assertEquals(clientDetailsDto, clientService.getClientDetails(1L));
    }

    @Test
    void givenEmailIsUsed_whenCreateClient_thenBadRequestException() {
        val clientCreationDto = ClientFixtures.getClientCreationDtoFixture();

        Mockito.when(userRepository.existsByEmail(clientCreationDto.getEmail())).thenReturn(true);

        val exc = Assertions.assertThrows(BadRequestException.class, () -> clientService.createClient(clientCreationDto));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("User with email " + clientCreationDto.getEmail() + " already exists", exc.getMessage());
    }

    @Test
    void whenCreateClient_thenCreateClient() {
        val clientCreationDto = ClientFixtures.getClientCreationDtoFixture();
        val client = ClientFixtures.getClientFixture();

        Mockito.when(userRepository.existsByEmail(clientCreationDto.getEmail())).thenReturn(false);
        Mockito.when(clientMapper.mapToClient(clientCreationDto)).thenReturn(client);

        Assertions.assertDoesNotThrow(() -> clientService.createClient(clientCreationDto));

        Mockito.verify(clientRepository, Mockito.times(1)).save(client);
    }

    @Test
    void givenDifferentClient_whenUpdateClient_thenForbiddenException() {
        val clientUpdateDto = ClientFixtures.getClientUpdateDtoFixture();
        val client = ClientFixtures.getClientFixture();
        client.setId(2L);

        Mockito.when(userInformationService.getCurrentUser()).thenReturn(client);

        val exc = Assertions.assertThrows(ForbiddenException.class, () -> clientService.updateClient(1L, clientUpdateDto));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Client not allowed to update other users", exc.getMessage());
    }

    @Test
    void whenUpdateClient_thenUpdateClient() {
        val clientUpdateDto = ClientFixtures.getClientUpdateDtoFixture();
        val client = ClientFixtures.getClientFixture();

        Mockito.when(userInformationService.getCurrentUser()).thenReturn(client);

        Assertions.assertDoesNotThrow(() -> clientService.updateClient(client.getId(), clientUpdateDto));

        Mockito.verify(clientMapper, Mockito.times(1)).mergeToClient(client, clientUpdateDto);
    }
}
