package ro.unibuc.fmi.awbd.fixtures;

import lombok.val;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.controller.models.CookCreationDto;
import ro.unibuc.fmi.awbd.controller.models.CookProductCreationDto;
import ro.unibuc.fmi.awbd.controller.models.CookProductUpdateDto;
import ro.unibuc.fmi.awbd.controller.models.CookUpdateDto;
import ro.unibuc.fmi.awbd.domain.user.model.cook.Cook;
import ro.unibuc.fmi.awbd.service.cook.model.CookPageElementDetails;

import java.util.List;

public class CookFixtures {
    private CookFixtures() {
    }

    public static Page<CookPageElementDetails> getPageOfCookPageElementDetailsFixture() {
        return Page.<CookPageElementDetails>builder()
                .items(List.of(getCookPageElementDetailsFixture()))
                .paginationInformation(PaginationInformationFixtures.getPaginationInformationFixture())
                .build();
    }

    public static Cook getCookFixture() {
        val cook = new Cook();
        cook.setId(1L);
        cook.setEmail("cook@cook.com");
        cook.setName("Cook Name");
        cook.setPassword("password");
        cook.setSalary(2000.);
        cook.setProducts(List.of(ProductFixtures.getProductFixture()));
        return cook;
    }

    public static CookCreationDto getCookCreationDtoFixture() {
        return new CookCreationDto("cook@new.com", "password", "Cook Name", 2000., List.of(getCookProductCreationDtoFixture()));
    }

    public static CookUpdateDto getCookUpdateDtoFixture() {
        return new CookUpdateDto("new password", "New Cook Name", 2500., List.of(getCookProductUpdateDtoFixture()));
    }

    private static CookPageElementDetails getCookPageElementDetailsFixture() {
        return CookPageElementDetails.builder()
                .id(1L)
                .email("cook@cook.com")
                .name("Cook Name")
                .salary(2000.)
                .build();
    }

    private static CookProductCreationDto getCookProductCreationDtoFixture() {
        return new CookProductCreationDto(1L);
    }

    private static CookProductUpdateDto getCookProductUpdateDtoFixture() {
        return new CookProductUpdateDto(2L);
    }
}
