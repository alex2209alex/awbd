package ro.unibuc.fmi.awbd.service.user;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.unibuc.fmi.awbd.common.exception.UnauthorizedException;
import ro.unibuc.fmi.awbd.common.security.JwtUtils;
import ro.unibuc.fmi.awbd.domain.user.repository.UserRepository;
import ro.unibuc.fmi.awbd.fixtures.ClientFixtures;
import ro.unibuc.fmi.awbd.fixtures.UserFixtures;
import ro.unibuc.fmi.awbd.service.user.mapper.UserMapper;

import java.lang.reflect.Field;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserService userService;

    @Test
    void givenUserNotFound_whenLoginUser_thenUnauthorizedException() {
        val userLoginDto = UserFixtures.getUserLoginDtoFixture();

        Mockito.when(userMapper.hashPassword(userLoginDto.getPassword())).thenReturn("hash");
        Mockito.when(userRepository.findByEmailAndPassword(userLoginDto.getEmail(), "hash")).thenReturn(Optional.empty());

        val exc = Assertions.assertThrows(UnauthorizedException.class, () -> userService.loginUser(userLoginDto));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Authorization failed", exc.getMessage());
    }

    @Test
    @SneakyThrows
    void whenLoginUser_thenLoginUser() {
        try (MockedStatic<JwtUtils> jwtUtils = Mockito.mockStatic(JwtUtils.class)) {
            val userLoginDto = UserFixtures.getUserLoginDtoFixture();
            val client = ClientFixtures.getClientFixture();
            val secret = "secret";
            Field secretField = UserService.class.getDeclaredField("secret");
            secretField.setAccessible(true);
            secretField.set(userService, secret);
            val token = "token";
            val tokenResponseDto = UserFixtures.getTokenResponseDtoFixture();

            Mockito.when(userMapper.hashPassword(userLoginDto.getPassword())).thenReturn("hash");
            Mockito.when(userRepository.findByEmailAndPassword(userLoginDto.getEmail(), "hash")).thenReturn(Optional.of(client));
            jwtUtils.when(() ->
                    JwtUtils.generateToken(client.getId(), client.getEmail(), client.getName(), client.getRole(), secret)
            ).thenReturn(token);
            Mockito.when(userMapper.mapToTokenResponseDto(token)).thenReturn(tokenResponseDto);

            Assertions.assertEquals(tokenResponseDto, userService.loginUser(userLoginDto));
        }
    }
}
