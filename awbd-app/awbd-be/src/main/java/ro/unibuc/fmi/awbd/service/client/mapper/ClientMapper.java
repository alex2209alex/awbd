package ro.unibuc.fmi.awbd.service.client.mapper;

import org.mapstruct.*;
import ro.unibuc.fmi.awbd.common.exception.InternalServerErrorException;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;
import ro.unibuc.fmi.awbd.controller.models.ClientCreationDto;
import ro.unibuc.fmi.awbd.controller.models.ClientDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.ClientUpdateDto;
import ro.unibuc.fmi.awbd.domain.user.model.client.Client;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClientMapper {
    ClientDetailsDto mapToClientDetailsDto(Client client);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "loyaltyCard", ignore = true)
    Client mapToClient(ClientCreationDto clientCreationDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "loyaltyCard", ignore = true)
    void mergeToClient(@MappingTarget Client client, ClientUpdateDto clientUpdateDto);

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
