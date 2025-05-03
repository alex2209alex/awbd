package ro.unibuc.fmi.awbd.service.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import ro.unibuc.fmi.awbd.common.exception.InternalServerErrorException;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;
import ro.unibuc.fmi.awbd.controller.models.TokenResponseDto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserMapper {
    TokenResponseDto mapToTokenResponseDto(String token);

    @Named("hashPassword")
    default String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            return new String(messageDigest.digest(password.getBytes(StandardCharsets.UTF_16)), StandardCharsets.UTF_16);
        } catch (NoSuchAlgorithmException exception) {
            throw new InternalServerErrorException(ErrorMessageUtils.ERROR_HASHING_ALGORITHM);
        }
    }
}
