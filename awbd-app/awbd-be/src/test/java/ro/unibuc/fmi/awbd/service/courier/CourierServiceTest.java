package ro.unibuc.fmi.awbd.service.courier;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.unibuc.fmi.awbd.common.exception.BadRequestException;
import ro.unibuc.fmi.awbd.common.exception.ForbiddenException;
import ro.unibuc.fmi.awbd.common.exception.NotFoundException;
import ro.unibuc.fmi.awbd.controller.models.CourierDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.CouriersPageDto;
import ro.unibuc.fmi.awbd.domain.onlineorder.model.OnlineOrder;
import ro.unibuc.fmi.awbd.domain.user.model.courier.Courier;
import ro.unibuc.fmi.awbd.domain.user.repository.UserRepository;
import ro.unibuc.fmi.awbd.domain.user.repository.courier.CourierRepository;
import ro.unibuc.fmi.awbd.domain.user.repository.courier.CourierSearchRepository;
import ro.unibuc.fmi.awbd.fixtures.CourierFixtures;
import ro.unibuc.fmi.awbd.service.courier.mapper.CourierMapper;
import ro.unibuc.fmi.awbd.service.user.UserInformationService;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {
    @Mock
    CourierSearchRepository courierSearchRepository;

    @Mock
    CourierRepository courierRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CourierMapper courierMapper;

    @Mock
    UserInformationService userInformationService;

    @InjectMocks
    CourierService courierService;

    @Test
    void whenGetCouriersPage_thenGetCouriersPage() {
        val pageRequest = CourierFixtures.getPageRequestFixture();
        val page = CourierFixtures.getPageOfCourierPageElementDetailsFixture();
        val couriersPageDto = new CouriersPageDto();

        Mockito.when(courierSearchRepository.getCouriersPage(pageRequest)).thenReturn(page);
        Mockito.when(courierMapper.mapToCouriersPageDto(page)).thenReturn(couriersPageDto);

        Assertions.assertEquals(couriersPageDto, courierService.getCouriersPage(pageRequest));
    }

    @Test
    void givenNonExistentCourier_whenGetCourierDetails_thenNotFoundException() {
        val courierId = 1L;

        Mockito.when(courierRepository.findById(courierId)).thenReturn(Optional.empty());

        val exc = Assertions.assertThrows(NotFoundException.class, () -> courierService.getCourierDetails(courierId));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Courier with ID " + courierId + " not found", exc.getMessage());
    }

    @Test
    void whenGetCourierDetails_thenGetCourierDetails() {
        val courierId = 1L;
        val courier = new Courier();
        val courierDetailsDto = new CourierDetailsDto();

        Mockito.when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));
        Mockito.when(courierMapper.mapToCourierDetailsDto(courier)).thenReturn(courierDetailsDto);

        Assertions.assertEquals(courierDetailsDto, courierService.getCourierDetails(courierId));
    }

    @Test
    void givenEmailIsUsed_whenCreateCourier_thenBadRequestException() {
        val courierCreationDto = CourierFixtures.getCourierCreationDtoFixture();

        Mockito.when(userRepository.existsByEmail(courierCreationDto.getEmail())).thenReturn(true);

        val exc = Assertions.assertThrows(BadRequestException.class, () -> courierService.createCourier(courierCreationDto));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("User with email " + courierCreationDto.getEmail() + " already exists", exc.getMessage());
    }

    @Test
    void whenCreateCourier_thenCreateCourier() {
        val courierCreationDto = CourierFixtures.getCourierCreationDtoFixture();
        val courier = CourierFixtures.getCourierFixture();

        Mockito.when(userRepository.existsByEmail(courierCreationDto.getEmail())).thenReturn(false);
        Mockito.when(courierMapper.mapToCourier(courierCreationDto)).thenReturn(courier);

        Assertions.assertDoesNotThrow(() -> courierService.createCourier(courierCreationDto));

        Mockito.verify(courierRepository, Mockito.times(1)).save(courier);
    }

    @Test
    void givenNonExistentCourier_whenUpdateCourier_thenNotFoundException() {
        val courierId = 1L;
        val courierUpdateDto = CourierFixtures.getCourierUpdateDtoFixture();

        Mockito.when(courierRepository.findById(courierId)).thenReturn(Optional.empty());

        val exc = Assertions.assertThrows(NotFoundException.class, () -> courierService.updateCourier(courierId, courierUpdateDto));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Courier with ID " + courierId + " not found", exc.getMessage());
    }

    @Test
    void whenUpdateCourier_thenUpdateCourier() {
        val courierId = 1L;
        val courierUpdateDto = CourierFixtures.getCourierUpdateDtoFixture();
        val courier = CourierFixtures.getCourierFixture();

        Mockito.when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));

        Assertions.assertDoesNotThrow(() -> courierService.updateCourier(courierId, courierUpdateDto));

        Mockito.verify(courierMapper, Mockito.times(1)).mergeToCourier(courier, courierUpdateDto);
    }

    @Test
    void givenNonExistentCourier_whenDeleteCourier_thenNotFoundException() {
        val courierId = 1L;

        Mockito.when(courierRepository.findById(courierId)).thenReturn(Optional.empty());

        val exc = Assertions.assertThrows(NotFoundException.class, () -> courierService.deleteCourier(courierId));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Courier with ID " + courierId + " not found", exc.getMessage());
    }

    @Test
    void givenProducerWithIngredients_whenDeleteProducer_thenForbiddenException() {
        val courierId = 1L;
        val courier = new Courier();
        courier.setOnlineOrders(List.of(new OnlineOrder()));

        Mockito.when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));

        val exc = Assertions.assertThrows(ForbiddenException.class, () -> courierService.deleteCourier(courierId));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Courier with ID " + courierId + " has dependencies and cannot be deleted", exc.getMessage());
    }

    @Test
    void whenDeleteCourier_thenDeleteCourier() {
        val courierId = 1L;
        val courier = CourierFixtures.getCourierFixture();

        Mockito.when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));

        Assertions.assertDoesNotThrow(() -> courierService.deleteCourier(courierId));

        Mockito.verify(courierRepository, Mockito.times(1)).delete(courier);
    }
}
