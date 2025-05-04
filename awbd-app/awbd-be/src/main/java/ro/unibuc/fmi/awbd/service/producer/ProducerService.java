package ro.unibuc.fmi.awbd.service.producer;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.awbd.common.exception.ForbiddenException;
import ro.unibuc.fmi.awbd.common.exception.NotFoundException;
import ro.unibuc.fmi.awbd.common.model.Page;
import ro.unibuc.fmi.awbd.common.model.PageRequest;
import ro.unibuc.fmi.awbd.common.utils.ErrorMessageUtils;
import ro.unibuc.fmi.awbd.controller.models.*;
import ro.unibuc.fmi.awbd.domain.producer.repository.ProducerRepository;
import ro.unibuc.fmi.awbd.domain.producer.repository.ProducerSearchRepository;
import ro.unibuc.fmi.awbd.service.producer.mapper.ProducerMapper;
import ro.unibuc.fmi.awbd.service.producer.model.ProducerFilter;
import ro.unibuc.fmi.awbd.service.producer.model.ProducerPageElementDetails;
import ro.unibuc.fmi.awbd.service.user.UserInformationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final ProducerSearchRepository producerSearchRepository;
    private final ProducerRepository producerRepository;
    private final ProducerMapper producerMapper;
    private final UserInformationService userInformationService;

    @Transactional(readOnly = true)
    public ProducersPageDto getProducersPage(PageRequest<ProducerFilter> pageRequest) {
        userInformationService.ensureCurrentUserIsRestaurantAdmin();
        Page<ProducerPageElementDetails> page = producerSearchRepository.getProducersPage(pageRequest);
        return producerMapper.mapToProducersPageDto(page);
    }

    @Transactional(readOnly = true)
    public List<ProducerSearchDetailsDto> getProducers() {
        userInformationService.ensureCurrentUserIsRestaurantAdmin();
        val producers = producerRepository.findAllByOrderByName();
        return producerMapper.mapToProducerSearchDetailsDtos(producers);
    }

    @Transactional(readOnly = true)
    public ProducerDetailsDto getProducerDetails(Long producerId) {
        userInformationService.ensureCurrentUserIsRestaurantAdmin();
        val producer = producerRepository.findById(producerId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.PRODUCER_NOT_FOUND, producerId))
        );
        return producerMapper.mapToProducerDetailsDto(producer);
    }

    @Transactional
    public void createProducer(ProducerCreationDto producerCreationDto) {
        userInformationService.ensureCurrentUserIsRestaurantAdmin();
        val producer = producerMapper.mapToProducer(producerCreationDto);
        producerRepository.save(producer);
    }

    @Transactional
    public void updateProducer(Long producerId, ProducerUpdateDto producerUpdateDto) {
        userInformationService.ensureCurrentUserIsRestaurantAdmin();
        val producer = producerRepository.findById(producerId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.PRODUCER_NOT_FOUND, producerId))
        );
        producerMapper.mergeToProducer(producer, producerUpdateDto);
    }

    @Transactional
    public void deleteProducer(Long producerId) {
        userInformationService.ensureCurrentUserIsRestaurantAdmin();
        val producer = producerRepository.findById(producerId).orElseThrow(() ->
                new NotFoundException(String.format(ErrorMessageUtils.PRODUCER_NOT_FOUND, producerId))
        );
        if (!producer.getIngredients().isEmpty()) {
            throw new ForbiddenException(String.format(ErrorMessageUtils.PRODUCER_HAS_DEPENDENCIES_AND_CANNOT_BE_DELETED, producerId));
        }
        producerRepository.delete(producer);
    }
}
