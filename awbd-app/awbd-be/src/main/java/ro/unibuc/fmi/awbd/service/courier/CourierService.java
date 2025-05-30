package ro.unibuc.fmi.awbd.service.courier;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.awbd.common.exception.BadRequestException;
import ro.unibuc.fmi.awbd.common.exception.ForbiddenException;
import ro.unibuc.fmi.awbd.common.exception.NotFoundException;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;
import ro.unibuc.fmi.awbd.controller.models.CourierCreationDto;
import ro.unibuc.fmi.awbd.controller.models.CourierDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.CourierUpdateDto;
import ro.unibuc.fmi.awbd.controller.models.CouriersPageDto;
import ro.unibuc.fmi.awbd.domain.user.repository.UserRepository;
import ro.unibuc.fmi.awbd.domain.user.repository.courier.CourierRepository;
import ro.unibuc.fmi.awbd.domain.user.repository.courier.CourierSearchRepository;
import ro.unibuc.fmi.awbd.service.courier.mapper.CourierMapper;
import ro.unibuc.fmi.awbd.service.courier.model.CourierFilter;
import ro.unibuc.fmi.awbd.service.courier.model.CourierPageElementDetails;
import ro.unibuc.fmi.awbd.service.user.UserInformationService;

@Service
@RequiredArgsConstructor
public class CourierService {
    private final CourierSearchRepository courierSearchRepository;
    private final CourierRepository courierRepository;
    private final UserRepository userRepository;
    private final CourierMapper courierMapper;
    private final UserInformationService userInformationService;

    @Transactional(readOnly = true)
    public CouriersPageDto getCouriersPage(PageRequest<CourierFilter> pageRequest) {
        userInformationService.ensureCurrentUserIsRestaurantAdmin();
        Page<CourierPageElementDetails> page = courierSearchRepository.getCouriersPage(pageRequest);
        return courierMapper.mapToCouriersPageDto(page);
    }

    @Transactional(readOnly = true)
    public CourierDetailsDto getCourierDetails(Long courierId) {
        userInformationService.ensureCurrentUserIsRestaurantAdmin();
        val courier = courierRepository.findById(courierId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.COURIER_NOT_FOUND, courierId))
        );
        return courierMapper.mapToCourierDetailsDto(courier);
    }

    @Transactional
    public void createCourier(CourierCreationDto courierCreationDto) {
        userInformationService.ensureCurrentUserIsRestaurantAdmin();
        if (userRepository.existsByEmail(courierCreationDto.getEmail())) {
            throw new BadRequestException(String.format(ErrorMessageUtils.USER_WITH_EMAIL_ALREADY_EXISTS, courierCreationDto.getEmail()));
        }
        val courier = courierMapper.mapToCourier(courierCreationDto);
        courierRepository.save(courier);
    }

    @Transactional
    public void updateCourier(Long courierId, CourierUpdateDto courierUpdateDto) {
        userInformationService.ensureCurrentUserIsRestaurantAdmin();
        val courier = courierRepository.findById(courierId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.COURIER_NOT_FOUND, courierId))
        );
        courierMapper.mergeToCourier(courier, courierUpdateDto);
    }

    @Transactional
    public void deleteCourier(Long courierId) {
        userInformationService.ensureCurrentUserIsRestaurantAdmin();
        val courier = courierRepository.findById(courierId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.COURIER_NOT_FOUND, courierId))
        );
        if (!courier.getOnlineOrders().isEmpty()) {
            throw new ForbiddenException(String.format(ErrorMessageUtils.COURIER_HAS_DEPENDENCIES_AND_CANNOT_BE_DELETED, courierId));
        }
        courierRepository.delete(courier);
    }
}
