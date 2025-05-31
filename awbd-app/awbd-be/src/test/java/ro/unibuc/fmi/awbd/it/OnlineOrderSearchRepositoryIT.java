package ro.unibuc.fmi.awbd.it;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.model.PaginationRequest;
import ro.unibuc.fmi.awbd.config.IntegrationTest;
import ro.unibuc.fmi.awbd.domain.onlineorder.model.OnlineOrderStatus;
import ro.unibuc.fmi.awbd.domain.onlineorder.repository.OnlineOrderSearchRepository;

@IntegrationTest
@Sql(scripts = {"/db/online_order_search_repository_test.sql"})
@Transactional
class OnlineOrderSearchRepositoryIT {
    @Autowired
    private OnlineOrderSearchRepository onlineOrderSearchRepository;

    @Test
    @WithMockUser(value = "2", authorities = "CLIENT")
    void givenUserIsClient_whenGetOnlineOrdersPage_thenReturnOnlineOrdersPageOfClient() {
        val paginationRequest = PaginationRequest.builder()
                .page(1)
                .pageSize(10)
                .sort("-creation_time")
                .build();
        val pageRequest = PageRequest.of(new Object(), paginationRequest);

        var onlineOrdersPage = onlineOrderSearchRepository.getOnlineOrdersPage(pageRequest);

        Assertions.assertNotNull(onlineOrdersPage);
        Assertions.assertNotNull(onlineOrdersPage.getItems());
        Assertions.assertEquals(1, onlineOrdersPage.getItems().size());
        Assertions.assertEquals(1, onlineOrdersPage.getItems().getFirst().getId());
        Assertions.assertEquals("OnlineOrder 1 address", onlineOrdersPage.getItems().getFirst().getAddress());
        Assertions.assertEquals("client@client.com", onlineOrdersPage.getItems().getFirst().getClient());
        Assertions.assertEquals("courier@courier.com", onlineOrdersPage.getItems().getFirst().getCourier());
        Assertions.assertEquals(OnlineOrderStatus.ON_DELIVERY.name(), onlineOrdersPage.getItems().getFirst().getStatus());
        Assertions.assertEquals(100., onlineOrdersPage.getItems().getFirst().getPrice());
    }

    @Test
    @WithMockUser(value = "5", authorities = "COOK")
    void givenUserIsCook_whenGetOnlineOrdersPage_thenReturnOnlineOrdersPageOfCook() {
        val paginationRequest = PaginationRequest.builder()
                .page(1)
                .pageSize(10)
                .sort("-creation_time")
                .build();
        val pageRequest = PageRequest.of(new Object(), paginationRequest);

        var onlineOrdersPage = onlineOrderSearchRepository.getOnlineOrdersPage(pageRequest);

        Assertions.assertNotNull(onlineOrdersPage);
        Assertions.assertNotNull(onlineOrdersPage.getItems());
        Assertions.assertEquals(1, onlineOrdersPage.getItems().size());
        Assertions.assertEquals(2, onlineOrdersPage.getItems().getFirst().getId());
        Assertions.assertEquals("OnlineOrder 2 address", onlineOrdersPage.getItems().getFirst().getAddress());
        Assertions.assertEquals("client2@client2.com", onlineOrdersPage.getItems().getFirst().getClient());
        Assertions.assertNull(onlineOrdersPage.getItems().getFirst().getCourier());
        Assertions.assertEquals(OnlineOrderStatus.IN_PREPARATION.name(), onlineOrdersPage.getItems().getFirst().getStatus());
        Assertions.assertEquals(200., onlineOrdersPage.getItems().getFirst().getPrice());
    }

    @Test
    @WithMockUser(value = "3", authorities = "COURIER")
    void givenUserIsCourier_whenGetOnlineOrdersPage_thenReturnOnlineOrdersPageOfCourier() {
        val paginationRequest = PaginationRequest.builder()
                .page(1)
                .pageSize(10)
                .sort("-creation_time")
                .build();
        val pageRequest = PageRequest.of(new Object(), paginationRequest);

        var onlineOrdersPage = onlineOrderSearchRepository.getOnlineOrdersPage(pageRequest);

        Assertions.assertNotNull(onlineOrdersPage);
        Assertions.assertNotNull(onlineOrdersPage.getItems());
        Assertions.assertEquals(2, onlineOrdersPage.getItems().size());
        Assertions.assertEquals(1, onlineOrdersPage.getItems().getFirst().getId());
        Assertions.assertEquals("OnlineOrder 1 address", onlineOrdersPage.getItems().getFirst().getAddress());
        Assertions.assertEquals("client@client.com", onlineOrdersPage.getItems().getFirst().getClient());
        Assertions.assertEquals("courier@courier.com", onlineOrdersPage.getItems().getFirst().getCourier());
        Assertions.assertEquals(OnlineOrderStatus.ON_DELIVERY.name(), onlineOrdersPage.getItems().getFirst().getStatus());
        Assertions.assertEquals(100., onlineOrdersPage.getItems().getFirst().getPrice());
        Assertions.assertEquals(3, onlineOrdersPage.getItems().get(1).getId());
        Assertions.assertEquals("OnlineOrder 3 address", onlineOrdersPage.getItems().get(1).getAddress());
        Assertions.assertEquals("client2@client2.com", onlineOrdersPage.getItems().get(1).getClient());
        Assertions.assertNull(onlineOrdersPage.getItems().get(1).getCourier());
        Assertions.assertEquals(OnlineOrderStatus.READY.name(), onlineOrdersPage.getItems().get(1).getStatus());
        Assertions.assertEquals(300., onlineOrdersPage.getItems().get(1).getPrice());
    }

    @Test
    @WithMockUser(value = "1", authorities = "RESTAURANT_ADMIN")
    void givenUserIsRestaurantAdmin_whenGetOnlineOrdersPage_thenReturnOnlineOrdersPageOfCourier() {
        val paginationRequest = PaginationRequest.builder()
                .page(1)
                .pageSize(10)
                .sort("-creation_time")
                .build();
        val pageRequest = PageRequest.of(new Object(), paginationRequest);

        var onlineOrdersPage = onlineOrderSearchRepository.getOnlineOrdersPage(pageRequest);

        Assertions.assertNotNull(onlineOrdersPage);
        Assertions.assertNotNull(onlineOrdersPage.getItems());
        Assertions.assertEquals(3, onlineOrdersPage.getItems().size());
        Assertions.assertEquals(1, onlineOrdersPage.getItems().getFirst().getId());
        Assertions.assertEquals("OnlineOrder 1 address", onlineOrdersPage.getItems().getFirst().getAddress());
        Assertions.assertEquals("client@client.com", onlineOrdersPage.getItems().getFirst().getClient());
        Assertions.assertEquals("courier@courier.com", onlineOrdersPage.getItems().getFirst().getCourier());
        Assertions.assertEquals(OnlineOrderStatus.ON_DELIVERY.name(), onlineOrdersPage.getItems().getFirst().getStatus());
        Assertions.assertEquals(100., onlineOrdersPage.getItems().getFirst().getPrice());
        Assertions.assertEquals(2, onlineOrdersPage.getItems().get(1).getId());
        Assertions.assertEquals("OnlineOrder 2 address", onlineOrdersPage.getItems().get(1).getAddress());
        Assertions.assertEquals("client2@client2.com", onlineOrdersPage.getItems().get(1).getClient());
        Assertions.assertNull(onlineOrdersPage.getItems().get(1).getCourier());
        Assertions.assertEquals(OnlineOrderStatus.IN_PREPARATION.name(), onlineOrdersPage.getItems().get(1).getStatus());
        Assertions.assertEquals(200., onlineOrdersPage.getItems().get(1).getPrice());
        Assertions.assertEquals(3, onlineOrdersPage.getItems().get(2).getId());
        Assertions.assertEquals("OnlineOrder 3 address", onlineOrdersPage.getItems().get(2).getAddress());
        Assertions.assertEquals("client2@client2.com", onlineOrdersPage.getItems().get(2).getClient());
        Assertions.assertNull(onlineOrdersPage.getItems().get(2).getCourier());
        Assertions.assertEquals(OnlineOrderStatus.READY.name(), onlineOrdersPage.getItems().get(2).getStatus());
        Assertions.assertEquals(300., onlineOrdersPage.getItems().get(2).getPrice());
    }
}
