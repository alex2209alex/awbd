package ro.unibuc.fmi.awbd.it;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.model.PaginationRequest;
import ro.unibuc.fmi.awbd.config.IntegrationTest;
import ro.unibuc.fmi.awbd.domain.user.repository.courier.CourierSearchRepository;
import ro.unibuc.fmi.awbd.service.courier.model.CourierFilter;

@IntegrationTest
@Sql(scripts = {"/db/courier_search_repository_test.sql"})
@Transactional
class CourierSearchRepositoryIT {
    @Autowired
    private CourierSearchRepository courierSearchRepository;

    @Test
    void givenNoSearchParameters_whenGetCouriersPage_thenReturnCouriersPage() {
        val paginationRequest = PaginationRequest.builder()
                .page(1)
                .pageSize(10)
                .sort("+email")
                .build();
        val filter = new CourierFilter();
        val pageRequest = PageRequest.of(filter, paginationRequest);

        var couriersPage = courierSearchRepository.getCouriersPage(pageRequest);

        Assertions.assertNotNull(couriersPage);
        Assertions.assertNotNull(couriersPage.getItems());
        Assertions.assertEquals(2, couriersPage.getItems().size());
        Assertions.assertEquals(2, couriersPage.getItems().getFirst().getId());
        Assertions.assertEquals("Courier 2", couriersPage.getItems().getFirst().getName());
        Assertions.assertEquals("courier2@courier2.com", couriersPage.getItems().getFirst().getEmail());
        Assertions.assertEquals("0787654321", couriersPage.getItems().getFirst().getPhoneNumber());
        Assertions.assertEquals(2500., couriersPage.getItems().getFirst().getSalary());
        Assertions.assertEquals(1, couriersPage.getItems().get(1).getId());
        Assertions.assertEquals("Courier 1", couriersPage.getItems().get(1).getName());
        Assertions.assertEquals("courier@courier.com", couriersPage.getItems().get(1).getEmail());
        Assertions.assertEquals("0712345678", couriersPage.getItems().get(1).getPhoneNumber());
        Assertions.assertEquals(2000., couriersPage.getItems().get(1).getSalary());

        paginationRequest.setSort("+phone_number");

        couriersPage = courierSearchRepository.getCouriersPage(pageRequest);

        Assertions.assertNotNull(couriersPage);
        Assertions.assertNotNull(couriersPage.getItems());
        Assertions.assertEquals(2, couriersPage.getItems().size());
        Assertions.assertEquals(1, couriersPage.getItems().getFirst().getId());
        Assertions.assertEquals(2, couriersPage.getItems().get(1).getId());

        paginationRequest.setSort("-salary");

        couriersPage = courierSearchRepository.getCouriersPage(pageRequest);

        Assertions.assertNotNull(couriersPage);
        Assertions.assertNotNull(couriersPage.getItems());
        Assertions.assertEquals(2, couriersPage.getItems().size());
        Assertions.assertEquals(2, couriersPage.getItems().getFirst().getId());
        Assertions.assertEquals(1, couriersPage.getItems().get(1).getId());
    }

    @Test
    void givenSearchParameters_whenGetCouriersPage_thenReturnCouriersPage() {
        val paginationRequest = PaginationRequest.builder()
                .page(1)
                .pageSize(10)
                .sort("+name")
                .build();
        val filter = CourierFilter.builder()
                .name("Courier 1")
                .email("courier")
                .phoneNumber("07123")
                .build();
        val pageRequest = PageRequest.of(filter, paginationRequest);

        val couriersPage = courierSearchRepository.getCouriersPage(pageRequest);

        Assertions.assertNotNull(couriersPage);
        Assertions.assertNotNull(couriersPage.getItems());
        Assertions.assertEquals(1, couriersPage.getItems().size());
        Assertions.assertEquals(1, couriersPage.getItems().getFirst().getId());
        Assertions.assertEquals("Courier 1", couriersPage.getItems().getFirst().getName());
        Assertions.assertEquals("courier@courier.com", couriersPage.getItems().getFirst().getEmail());
        Assertions.assertEquals("0712345678", couriersPage.getItems().getFirst().getPhoneNumber());
        Assertions.assertEquals(2000., couriersPage.getItems().getFirst().getSalary());
    }
}
