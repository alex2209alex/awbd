package ro.unibuc.fmi.awbd.service.courier.mapper;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.unibuc.fmi.awbd.common.exception.InternalServerErrorException;
import ro.unibuc.fmi.awbd.domain.user.model.Role;
import ro.unibuc.fmi.awbd.fixtures.CourierFixtures;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest(classes = {CourierMapperImpl.class})
class CourierMapperTest {
    @Autowired
    private CourierMapper courierMapper;

    @Test
    void testMapToCourierFilter() {
        Assertions.assertNull(courierMapper.mapToCourierFilter(null, null, null));

        val email = "email";
        val name = "name";
        val phoneNumber = "phoneNumber";

        var courierFilter = courierMapper.mapToCourierFilter(email, name, phoneNumber);

        Assertions.assertNotNull(courierFilter);
        Assertions.assertEquals(email, courierFilter.getEmail());
        Assertions.assertEquals(name, courierFilter.getName());
        Assertions.assertEquals(phoneNumber, courierFilter.getPhoneNumber());

        courierFilter = courierMapper.mapToCourierFilter(null, name, phoneNumber);

        Assertions.assertNotNull(courierFilter);
        Assertions.assertNull(courierFilter.getEmail());
        Assertions.assertEquals(name, courierFilter.getName());
        Assertions.assertEquals(phoneNumber, courierFilter.getPhoneNumber());

        courierFilter = courierMapper.mapToCourierFilter(email, null, phoneNumber);

        Assertions.assertNotNull(courierFilter);
        Assertions.assertEquals(email, courierFilter.getEmail());
        Assertions.assertNull(courierFilter.getName());
        Assertions.assertEquals(phoneNumber, courierFilter.getPhoneNumber());

        courierFilter = courierMapper.mapToCourierFilter(email, name, null);

        Assertions.assertNotNull(courierFilter);
        Assertions.assertEquals(email, courierFilter.getEmail());
        Assertions.assertEquals(name, courierFilter.getName());
        Assertions.assertNull(courierFilter.getPhoneNumber());
    }

    @Test
    void testMapToCouriersPageDto() {
        Assertions.assertNull(courierMapper.mapToCouriersPageDto(null));

        val page = CourierFixtures.getPageOfCourierPageElementDetailsFixture();

        var couriersPageDto = courierMapper.mapToCouriersPageDto(page);

        Assertions.assertNotNull(couriersPageDto);

        Assertions.assertNotNull(couriersPageDto.getPagination());
        Assertions.assertEquals(couriersPageDto.getPagination().getPage(), page.getPaginationInformation().getPage());
        Assertions.assertEquals(couriersPageDto.getPagination().getPageSize(), page.getPaginationInformation().getPageSize());
        Assertions.assertEquals(couriersPageDto.getPagination().getPagesTotal(), page.getPaginationInformation().getPagesTotal());
        Assertions.assertEquals(couriersPageDto.getPagination().getHasNextPage(), page.getPaginationInformation().isHasNextPage());
        Assertions.assertEquals(couriersPageDto.getPagination().getItemsTotal(), page.getPaginationInformation().getItemsTotal());
        Assertions.assertEquals(couriersPageDto.getPagination().getSort(), page.getPaginationInformation().getSort());

        Assertions.assertNotNull(couriersPageDto.getItems());
        Assertions.assertEquals(page.getItems().size(), couriersPageDto.getItems().size());
        for (int i = 0; i < couriersPageDto.getItems().size(); i++) {
            Assertions.assertEquals(page.getItems().get(i).getId(), couriersPageDto.getItems().get(i).getId());
            Assertions.assertEquals(page.getItems().get(i).getEmail(), couriersPageDto.getItems().get(i).getEmail());
            Assertions.assertEquals(page.getItems().get(i).getName(), couriersPageDto.getItems().get(i).getName());
            Assertions.assertEquals(page.getItems().get(i).getPhoneNumber(), couriersPageDto.getItems().get(i).getPhoneNumber());
            Assertions.assertEquals(page.getItems().get(i).getSalary(), couriersPageDto.getItems().get(i).getSalary());
        }
    }

    @Test
    void testMapToCourierDetailsDto() {
        Assertions.assertNull(courierMapper.mapToCourierDetailsDto(null));

        val courier = CourierFixtures.getCourierFixture();

        val courierDetailsDto = courierMapper.mapToCourierDetailsDto(courier);

        Assertions.assertNotNull(courierDetailsDto);
        Assertions.assertEquals(courier.getId(), courierDetailsDto.getId());
        Assertions.assertEquals(courier.getEmail(), courierDetailsDto.getEmail());
        Assertions.assertEquals(courier.getName(), courierDetailsDto.getName());
        Assertions.assertEquals(courier.getPhoneNumber(), courierDetailsDto.getPhoneNumber());
        Assertions.assertEquals(courier.getSalary(), courierDetailsDto.getSalary());
    }

    @Test
    void testMapToCourier() {
        Assertions.assertNull(courierMapper.mapToCourier(null));

        val courierCreationDto = CourierFixtures.getCourierCreationDtoFixture();

        val courier = courierMapper.mapToCourier(courierCreationDto);

        Assertions.assertNotNull(courier);
        Assertions.assertEquals(courierCreationDto.getEmail(), courier.getEmail());
        Assertions.assertNotNull(courier.getPassword());
        Assertions.assertEquals(courierCreationDto.getName(), courier.getName());
        Assertions.assertEquals(Role.COURIER, courier.getRole());
        Assertions.assertEquals(courierCreationDto.getPhoneNumber(), courier.getPhoneNumber());
        Assertions.assertEquals(courierCreationDto.getSalary(), courier.getSalary());
    }

    @Test
    void testMergeToCourier() {
        val courier = CourierFixtures.getCourierFixture();

        Assertions.assertDoesNotThrow(() -> courierMapper.mergeToCourier(courier, null));

        val oldPassword = courier.getPassword();
        val courierUpdateDto = CourierFixtures.getCourierUpdateDtoFixture();

        courierMapper.mergeToCourier(courier, courierUpdateDto);

        Assertions.assertNotEquals(courier.getPassword(), oldPassword);
        Assertions.assertEquals(courier.getName(), courierUpdateDto.getName());
        Assertions.assertEquals(courier.getPhoneNumber(), courierUpdateDto.getPhoneNumber());
        Assertions.assertEquals(courier.getSalary(), courierUpdateDto.getSalary());
    }

    @Test
    void givenErrorWithHashingAlgorithm_whenHashPassword_thenInternalServerErrorException() {
        try (MockedStatic<MessageDigest> mocked = mockStatic(MessageDigest.class)) {
            mocked.when(() -> MessageDigest.getInstance(any()))
                    .thenThrow(new NoSuchAlgorithmException());

            val exc = Assertions.assertThrows(InternalServerErrorException.class, () -> courierMapper.hashPassword(""));

            Assertions.assertNotNull(exc);
            Assertions.assertEquals("Error with hashing algorithm", exc.getMessage());
        }
    }
}
