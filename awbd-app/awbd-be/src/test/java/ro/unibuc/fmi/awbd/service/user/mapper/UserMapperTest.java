package ro.unibuc.fmi.awbd.service.user.mapper;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.unibuc.fmi.awbd.common.exception.InternalServerErrorException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest(classes = {UserMapperImpl.class})
class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    @Test
    void testMapToTokenResponseDto() {
        Assertions.assertNull(userMapper.mapToTokenResponseDto(null));

        val tokenResponseDto = userMapper.mapToTokenResponseDto("token");

        Assertions.assertNotNull(tokenResponseDto);
        Assertions.assertEquals("token", tokenResponseDto.getToken());
    }

    @Test
    void givenErrorWithHashingAlgorithm_whenHashPassword_thenInternalServerErrorException() {
        try (MockedStatic<MessageDigest> mocked = mockStatic(MessageDigest.class)) {
            mocked.when(() -> MessageDigest.getInstance(any()))
                    .thenThrow(new NoSuchAlgorithmException());

            val exc = Assertions.assertThrows(InternalServerErrorException.class, () -> userMapper.hashPassword(""));

            Assertions.assertNotNull(exc);
            Assertions.assertEquals("Error with hashing algorithm", exc.getMessage());
        }
    }
}
