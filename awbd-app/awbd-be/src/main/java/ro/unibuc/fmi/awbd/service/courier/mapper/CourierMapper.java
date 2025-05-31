package ro.unibuc.fmi.awbd.service.courier.mapper;

import org.mapstruct.*;
import ro.unibuc.fmi.awbd.common.exception.InternalServerErrorException;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;
import ro.unibuc.fmi.awbd.controller.models.CourierCreationDto;
import ro.unibuc.fmi.awbd.controller.models.CourierDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.CourierUpdateDto;
import ro.unibuc.fmi.awbd.controller.models.CouriersPageDto;
import ro.unibuc.fmi.awbd.domain.user.model.courier.Courier;
import ro.unibuc.fmi.awbd.service.courier.model.CourierFilter;
import ro.unibuc.fmi.awbd.service.courier.model.CourierPageElementDetails;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CourierMapper {
    CourierFilter mapToCourierFilter(String email, String name, String phoneNumber);

    @Mapping(target = "pagination", source = "paginationInformation")
    CouriersPageDto mapToCouriersPageDto(Page<CourierPageElementDetails> page);

    CourierDetailsDto mapToCourierDetailsDto(Courier courier);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "role", expression = "java(ro.unibuc.fmi.awbd.domain.user.model.Role.COURIER)")
    @Mapping(target = "onlineOrders", ignore = true)
    Courier mapToCourier(CourierCreationDto courierCreationDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", source = "password", qualifiedByName = "hashPassword")
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "onlineOrders", ignore = true)
    void mergeToCourier(@MappingTarget Courier courier, CourierUpdateDto courierUpdateDto);

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
