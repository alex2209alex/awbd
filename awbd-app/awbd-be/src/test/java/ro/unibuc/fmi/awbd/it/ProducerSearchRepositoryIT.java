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
import ro.unibuc.fmi.awbd.domain.producer.repository.ProducerSearchRepository;
import ro.unibuc.fmi.awbd.service.producer.model.ProducerFilter;

@IntegrationTest
@Sql(scripts = {"/db/producer_search_repository_test.sql"})
@Transactional
class ProducerSearchRepositoryIT {
    @Autowired
    private ProducerSearchRepository producerSearchRepository;

    @Test
    void givenSearchParameters_whenGetProducersPage_thenReturnProducersPage() {
        val paginationRequest = PaginationRequest.builder()
                .page(1)
                .pageSize(10)
                .sort("+name")
                .build();
        val filter = ProducerFilter.builder()
                .name("ood F")
                .address("Str.")
                .build();
        val pageRequest = PageRequest.of(filter, paginationRequest);

        val producersPage = producerSearchRepository.getProducersPage(pageRequest);

        Assertions.assertNotNull(producersPage);
        Assertions.assertNotNull(producersPage.getItems());
        Assertions.assertEquals(1, producersPage.getItems().size());
        Assertions.assertEquals(1, producersPage.getItems().getFirst().getId());
        Assertions.assertEquals("Good Farm", producersPage.getItems().getFirst().getName());
        Assertions.assertEquals("Str. 1 Nr. 1", producersPage.getItems().getFirst().getAddress());
    }
}
