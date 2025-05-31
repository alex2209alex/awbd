package ro.unibuc.fmi.awbd.fixtures;

import lombok.val;
import ro.unibuc.fmi.awbd.controller.models.TokenResponseDto;
import ro.unibuc.fmi.awbd.controller.models.UserLoginDto;

public class UserFixtures {
    private UserFixtures() {
    }

    public static UserLoginDto getUserLoginDtoFixture() {
        return new UserLoginDto("email", "password");
    }

    public static UserLoginDto getUserLoginDtoFixture(String email, String password) {
        return new UserLoginDto(email, password);
    }

    public static TokenResponseDto getTokenResponseDtoFixture() {
        val tokenResponseDto = new TokenResponseDto();
        tokenResponseDto.setToken("token");
        return tokenResponseDto;
    }
}
