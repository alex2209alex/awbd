package ro.unibuc.fmi.awbd.fixtures;

import lombok.val;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.model.PaginationRequest;
import ro.unibuc.fmi.awbd.controller.models.ProducerCreationDto;
import ro.unibuc.fmi.awbd.controller.models.ProducerPageElementDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.ProducerUpdateDto;
import ro.unibuc.fmi.awbd.domain.producer.model.Producer;
import ro.unibuc.fmi.awbd.service.producer.model.ProducerFilter;

import java.util.List;

public class ProducerFixtures {
    public static Page<ProducerPageElementDetailsDto> getPageOfProducerPageElementDetailsDtoFixture() {
        return Page.<ProducerPageElementDetailsDto>builder()
                .items(List.of(getProducerPageElementDetailsDtoFixture()))
                .paginationInformation(PaginationInformationFixtures.getPaginationInformationFixture())
                .build();
    }

    public static Producer getProducerFixture() {
        val producer = new Producer();
        producer.setId(1L);
        producer.setName("name");
        producer.setAddress("address");
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
        val paginationRequest = new PaginationRequest(1L, 1, "+name");
        return PageRequest.of(producerFilter, paginationRequest);
    }

    private static ProducerPageElementDetailsDto getProducerPageElementDetailsDtoFixture() {
        val producerPageElementDetailsDto = new ProducerPageElementDetailsDto();
        producerPageElementDetailsDto.setId(1L);
        producerPageElementDetailsDto.setName("name");
        producerPageElementDetailsDto.setAddress("address");
        return producerPageElementDetailsDto;
    }
}
