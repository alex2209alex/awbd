package ro.unibuc.fmi.awbd.service.client.mapper;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.unibuc.fmi.awbd.common.exception.InternalServerErrorException;
import ro.unibuc.fmi.awbd.domain.user.model.Role;
import ro.unibuc.fmi.awbd.fixtures.ClientFixtures;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest(classes = {ClientMapperImpl.class})
class ClientMapperTest {
    @Autowired
    private ClientMapper clientMapper;

    @Test
    void testMapToClientDetailsDto() {
        Assertions.assertNull(clientMapper.mapToClientDetailsDto(null));

        val client = ClientFixtures.getClientFixture();

        val clientDetailsDto = clientMapper.mapToClientDetailsDto(client);

        Assertions.assertNotNull(clientDetailsDto);
        Assertions.assertEquals(client.getId(), clientDetailsDto.getId());
        Assertions.assertEquals(client.getEmail(), clientDetailsDto.getEmail());
        Assertions.assertEquals(client.getName(), clientDetailsDto.getName());
        Assertions.assertEquals(client.getPhoneNumber(), clientDetailsDto.getPhoneNumber());
    }

    @Test
    void testMapToClient() {
        Assertions.assertNull(clientMapper.mapToClient(null));

        val clientCreationDto = ClientFixtures.getClientCreationDtoFixture();

        val client = clientMapper.mapToClient(clientCreationDto);

        Assertions.assertNotNull(client);
        Assertions.assertEquals(client.getEmail(), clientCreationDto.getEmail());
        Assertions.assertNotNull(client.getPassword());
        Assertions.assertEquals(client.getName(), clientCreationDto.getName());
        Assertions.assertEquals(Role.CLIENT, client.getRole());
        Assertions.assertEquals(client.getPhoneNumber(), clientCreationDto.getPhoneNumber());
    }

    @Test
    void testMergeToClient() {
        val client = ClientFixtures.getClientFixture();

        Assertions.assertDoesNotThrow(() -> clientMapper.mergeToClient(client, null));

        val oldPassword = client.getPassword();
        val clientUpdateDto = ClientFixtures.getClientUpdateDtoFixture();

        clientMapper.mergeToClient(client, clientUpdateDto);

        Assertions.assertNotEquals(client.getPassword(), oldPassword);
        Assertions.assertEquals(client.getName(), clientUpdateDto.getName());
        Assertions.assertEquals(client.getPhoneNumber(), clientUpdateDto.getPhoneNumber());
    }

    @Test
    void givenErrorWithHashingAlgorithm_whenHashPassword_thenInternalServerErrorException() {
        try (MockedStatic<MessageDigest> mocked = mockStatic(MessageDigest.class)) {
            mocked.when(() -> MessageDigest.getInstance(any()))
                    .thenThrow(new NoSuchAlgorithmException());

            val exc = Assertions.assertThrows(InternalServerErrorException.class, () -> clientMapper.hashPassword(""));

            Assertions.assertNotNull(exc);
            Assertions.assertEquals("Error with hashing algorithm", exc.getMessage());
        }
    }
}
