package ro.unibuc.fmi.awbd.service.producer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.controller.models.*;
import ro.unibuc.fmi.awbd.domain.producer.model.Producer;
import ro.unibuc.fmi.awbd.service.producer.model.ProducerFilter;
import ro.unibuc.fmi.awbd.service.producer.model.ProducerPageElementDetails;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProducerMapper {
    ProducerFilter mapToProducerFilter(String name, String address);

    @Mapping(target = "pagination", source = "paginationInformation")
    ProducersPageDto mapToProducersPageDto(Page<ProducerPageElementDetails> page);

    List<ProducerSearchDetailsDto> mapToProducerSearchDetailsDtos(List<Producer> producers);

    ProducerDetailsDto mapToProducerDetailsDto(Producer producer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ingredients", ignore = true)
    Producer mapToProducer(ProducerCreationDto producerCreationDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ingredients", ignore = true)
    void mergeToProducer(@MappingTarget Producer producer, ProducerUpdateDto producerUpdateDto);
}
