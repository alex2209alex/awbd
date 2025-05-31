package ro.unibuc.fmi.awbd.fixtures;

import ro.unibuc.fmi.awbd.controller.models.CourierCreationDto;
import ro.unibuc.fmi.awbd.controller.models.CourierUpdateDto;

public class CourierFixtures {
    private CourierFixtures() {
    }

    public static CourierCreationDto getCourierCreationDtoFixture() {
        return new CourierCreationDto("courier@new.com", "password", "Courier Name", "0712312312", 2000.);
    }

    public static CourierUpdateDto getCourierUpdateDtoFixture() {
        return new CourierUpdateDto("new password", "New Courier Name", "0700000000", 2500.);
    }
}
