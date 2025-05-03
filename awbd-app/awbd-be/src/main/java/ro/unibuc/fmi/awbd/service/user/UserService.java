package ro.unibuc.fmi.awbd.service.user;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.awbd.common.exception.UnauthorizedException;
import ro.unibuc.fmi.awbd.common.security.JwtUtils;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;
import ro.unibuc.fmi.awbd.controller.models.TokenResponseDto;
import ro.unibuc.fmi.awbd.controller.models.UserLoginDto;
import ro.unibuc.fmi.awbd.domain.user.repository.UserRepository;
import ro.unibuc.fmi.awbd.service.user.mapper.UserMapper;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Value(value = "${jwt.secret}")
    private String secret;

    @Transactional
    public TokenResponseDto loginUser(UserLoginDto userLoginDto) {
        String password = userMapper.hashPassword(userLoginDto.getPassword());
        val user = userRepository.findByEmailAndPassword(userLoginDto.getEmail(), password)
                .orElseThrow(() -> new UnauthorizedException(String.format(ErrorMessageUtils.AUTHORIZATION_FAILED)));

        val token = JwtUtils.generateToken(user.getId(), user.getEmail(), user.getName(), user.getRole(), secret);

        return userMapper.mapToTokenResponseDto(token);
    }
}
