package ro.unibuc.fmi.awbd.fixtures;

import lombok.val;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.model.PaginationRequest;
import ro.unibuc.fmi.awbd.controller.models.ProducerCreationDto;
import ro.unibuc.fmi.awbd.controller.models.ProducerUpdateDto;
import ro.unibuc.fmi.awbd.domain.producer.model.Producer;
import ro.unibuc.fmi.awbd.service.producer.model.ProducerFilter;
import ro.unibuc.fmi.awbd.service.producer.model.ProducerPageElementDetails;

import java.util.ArrayList;
import java.util.List;

public class ProducerFixtures {
    private ProducerFixtures() {
    }

    public static Page<ProducerPageElementDetails> getPageOfProducerPageElementDetailsFixture() {
        return Page.<ProducerPageElementDetails>builder()
                .items(List.of(getProducerPageElementDetailsFixture()))
                .paginationInformation(PaginationInformationFixtures.getPaginationInformationFixture())
                .build();
    }

    public static Producer getProducerFixture() {
        val producer = new Producer();
        producer.setId(1L);
        producer.setName("name");
        producer.setAddress("address");
        producer.setIngredients(new ArrayList<>());
        return producer;
    }

    public static ProducerCreationDto getProducerCreationDtoFixture() {
        return new ProducerCreationDto("name", "address");
    }

    public static ProducerUpdateDto getProducerUpdateDtoFixture() {
        return new ProducerUpdateDto("name", "address");
    }

    public static PageRequest<ProducerFilter> getPageRequestFixture() {
        val producerFilter = new ProducerFilter("name", "address");
        val paginationRequest = new PaginationRequest(1, 1, "+name");
        return PageRequest.of(producerFilter, paginationRequest);
    }

    private static ProducerPageElementDetails getProducerPageElementDetailsFixture() {
        return ProducerPageElementDetails.builder()
                .id(1L)
                .name("name")
                .address("address")
                .build();
    }
}
