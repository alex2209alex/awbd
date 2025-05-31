package ro.unibuc.fmi.awbd.fixtures;

import lombok.val;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.controller.models.CourierCreationDto;
import ro.unibuc.fmi.awbd.controller.models.CourierUpdateDto;
import ro.unibuc.fmi.awbd.domain.user.model.courier.Courier;
import ro.unibuc.fmi.awbd.service.courier.model.CourierPageElementDetails;

import java.util.List;

public class CourierFixtures {
    private CourierFixtures() {
    }

    public static Page<CourierPageElementDetails> getPageOfCourierPageElementDetailsFixture() {
        return Page.<CourierPageElementDetails>builder()
                .items(List.of(getCourierPageElementDetailsFixture()))
                .paginationInformation(PaginationInformationFixtures.getPaginationInformationFixture())
                .build();
    }

    public static CourierCreationDto getCourierCreationDtoFixture() {
        return new CourierCreationDto("courier@new.com", "password", "Courier Name", "0712312312", 2000.);
    }

    public static CourierUpdateDto getCourierUpdateDtoFixture() {
        return new CourierUpdateDto("new password", "New Courier Name", "0700000000", 2500.);
    }

    public static Courier getCourierFixture() {
        val courier = new Courier();
        courier.setId(1L);
        courier.setEmail("cook@cook.com");
        courier.setName("Cook Name");
        courier.setPassword("password");
        courier.setPhoneNumber("0712345678");
        courier.setSalary(2000.);
        return courier;
    }

    private static CourierPageElementDetails getCourierPageElementDetailsFixture() {
        return CourierPageElementDetails.builder()
                .id(1L)
                .email("courier@courier.com")
                .name("Courier Name")
                .phoneNumber("0712345678")
                .salary(2000.)
                .build();
    }
}
