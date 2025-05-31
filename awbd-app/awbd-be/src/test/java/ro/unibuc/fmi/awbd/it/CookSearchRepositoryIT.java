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
import ro.unibuc.fmi.awbd.domain.user.repository.cook.CookSearchRepository;
import ro.unibuc.fmi.awbd.service.cook.model.CookFilter;

@IntegrationTest
@Sql(scripts = {"/db/cook_search_repository_test.sql"})
@Transactional
class CookSearchRepositoryIT {
    @Autowired
    private CookSearchRepository cookSearchRepository;

    @Test
    void givenNoSearchParameters_whenGetCooksPage_thenReturnCooksPage() {
        val paginationRequest = PaginationRequest.builder()
                .page(1)
                .pageSize(10)
                .sort("+email")
                .build();
        val filter = new CookFilter();
        val pageRequest = PageRequest.of(filter, paginationRequest);

        var cooksPage = cookSearchRepository.getCooksPage(pageRequest);

        Assertions.assertNotNull(cooksPage);
        Assertions.assertNotNull(cooksPage.getItems());
        Assertions.assertEquals(2, cooksPage.getItems().size());
        Assertions.assertEquals(2, cooksPage.getItems().getFirst().getId());
        Assertions.assertEquals("Cook 2", cooksPage.getItems().getFirst().getName());
        Assertions.assertEquals("cook2@cook2.com", cooksPage.getItems().getFirst().getEmail());
        Assertions.assertEquals(2500., cooksPage.getItems().getFirst().getSalary());
        Assertions.assertEquals(1, cooksPage.getItems().get(1).getId());
        Assertions.assertEquals("Cook 1", cooksPage.getItems().get(1).getName());
        Assertions.assertEquals("cook@cook.com", cooksPage.getItems().get(1).getEmail());
        Assertions.assertEquals(2000., cooksPage.getItems().get(1).getSalary());

        paginationRequest.setSort("-salary");

        cooksPage = cookSearchRepository.getCooksPage(pageRequest);

        Assertions.assertNotNull(cooksPage);
        Assertions.assertNotNull(cooksPage.getItems());
        Assertions.assertEquals(2, cooksPage.getItems().size());
        Assertions.assertEquals(2, cooksPage.getItems().getFirst().getId());
        Assertions.assertEquals(1, cooksPage.getItems().get(1).getId());
    }

    @Test
    void givenSearchParameters_whenGetCooksPage_thenReturnCooksPage() {
        val paginationRequest = PaginationRequest.builder()
                .page(1)
                .pageSize(10)
                .sort("+name")
                .build();
        val filter = CookFilter.builder()
                .name("Cook 1")
                .email("cook")
                .build();
        val pageRequest = PageRequest.of(filter, paginationRequest);

        val cooksPage = cookSearchRepository.getCooksPage(pageRequest);

        Assertions.assertNotNull(cooksPage);
        Assertions.assertNotNull(cooksPage.getItems());
        Assertions.assertEquals(1, cooksPage.getItems().size());
        Assertions.assertEquals(1, cooksPage.getItems().getFirst().getId());
        Assertions.assertEquals("Cook 1", cooksPage.getItems().getFirst().getName());
        Assertions.assertEquals("cook@cook.com", cooksPage.getItems().getFirst().getEmail());
        Assertions.assertEquals(2000., cooksPage.getItems().getFirst().getSalary());
    }
}
