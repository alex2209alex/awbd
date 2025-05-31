package ro.unibuc.fmi.awbd.service.cook.mapper;

import org.mapstruct.*;
import ro.unibuc.fmi.awbd.common.exception.InternalServerErrorException;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;
import ro.unibuc.fmi.awbd.controller.models.CookCreationDto;
import ro.unibuc.fmi.awbd.controller.models.CookDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.CookUpdateDto;
import ro.unibuc.fmi.awbd.controller.models.CooksPageDto;
import ro.unibuc.fmi.awbd.domain.user.model.cook.Cook;
import ro.unibuc.fmi.awbd.service.cook.model.CookFilter;
import ro.unibuc.fmi.awbd.service.cook.model.CookPageElementDetails;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CookMapper {
    CookFilter mapToCookFilter(String email, String name);

    @Mapping(target = "pagination", source = "paginationInformation")
    CooksPageDto mapToCooksPageDto(Page<CookPageElementDetails> page);

    CookDetailsDto mapToCookDetailsDto(Cook cook);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "role", expression = "java(ro.unibuc.fmi.awbd.domain.user.model.Role.COOK)")
    @Mapping(target = "products", ignore = true)
    Cook mapToCook(CookCreationDto cookCreationDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "products", ignore = true)
    void mergeToCook(@MappingTarget Cook cook, CookUpdateDto cookUpdateDto);

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
