package ro.unibuc.fmi.awbd.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.awbd.common.utils.LogUtils;
import ro.unibuc.fmi.awbd.controller.api.UserApi;
import ro.unibuc.fmi.awbd.controller.models.TokenResponseDto;
import ro.unibuc.fmi.awbd.controller.models.UserLoginDto;
import ro.unibuc.fmi.awbd.service.user.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    public TokenResponseDto authenticateUser(
            @RequestBody UserLoginDto userLoginDto
    ){
        log.info(LogUtils.AUTHENTICATE_USER);

        return userService.loginUser(userLoginDto);
    }
}
