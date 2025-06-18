package ro.unibuc.fmi.awbd.service.producer;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.unibuc.fmi.awbd.common.exception.ForbiddenException;
import ro.unibuc.fmi.awbd.common.exception.NotFoundException;
import ro.unibuc.fmi.awbd.controller.models.ProducerDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.ProducerSearchDetailsDto;
import ro.unibuc.fmi.awbd.controller.models.ProducersPageDto;
import ro.unibuc.fmi.awbd.domain.ingredient.model.Ingredient;
import ro.unibuc.fmi.awbd.domain.producer.model.Producer;
import ro.unibuc.fmi.awbd.domain.producer.repository.ProducerRepository;
import ro.unibuc.fmi.awbd.domain.producer.repository.ProducerSearchRepository;
import ro.unibuc.fmi.awbd.fixtures.ProducerFixtures;
import ro.unibuc.fmi.awbd.service.producer.mapper.ProducerMapper;
import ro.unibuc.fmi.awbd.service.user.UserInformationService;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProducerServiceTest {
    @Mock
    ProducerSearchRepository producerSearchRepository;

    @Mock
    ProducerRepository producerRepository;

    @Mock
    ProducerMapper producerMapper;

    @Mock
    UserInformationService userInformationService;

    @InjectMocks
    ProducerService producerService;

    @Test
    void whenGetProducersPage_thenGetProducersPage() {
        val pageRequest = ProducerFixtures.getPageRequestFixture();
        val page = ProducerFixtures.getPageOfProducerPageElementDetailsFixture();
        val producersPageDto = new ProducersPageDto();

        Mockito.when(producerSearchRepository.getProducersPage(pageRequest)).thenReturn(page);
        Mockito.when(producerMapper.mapToProducersPageDto(page)).thenReturn(producersPageDto);

        Assertions.assertEquals(producersPageDto, producerService.getProducersPage(pageRequest));
    }

    @Test
    void whenGetProducers_thenGetProducers() {
        val producers = List.of(ProducerFixtures.getProducerFixture());
        List<ProducerSearchDetailsDto> producerSearchDetailsDtos = List.of();

        Mockito.when(producerRepository.findAllByOrderByName()).thenReturn(producers);
        Mockito.when(producerMapper.mapToProducerSearchDetailsDtos(producers)).thenReturn(producerSearchDetailsDtos);

        Assertions.assertEquals(producerSearchDetailsDtos, producerService.getProducers());
    }

    @Test
    void givenNonExistentProducer_whenGetProducerDetails_thenNotFoundException() {
        val producerId = 1L;

        Mockito.when(producerRepository.findById(producerId)).thenReturn(Optional.empty());

        val exc = Assertions.assertThrows(NotFoundException.class, () -> producerService.getProducerDetails(producerId));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Producer with ID " + producerId + " not found", exc.getMessage());
    }

    @Test
    void whenGetProducerDetails_thenGetProducerDetails() {
        val producerId = 1L;
        val producer = new Producer();
        val producerDetailsDto = new ProducerDetailsDto();

        Mockito.when(producerRepository.findById(producerId)).thenReturn(Optional.of(producer));
        Mockito.when(producerMapper.mapToProducerDetailsDto(producer)).thenReturn(producerDetailsDto);

        Assertions.assertEquals(producerDetailsDto, producerService.getProducerDetails(producerId));
    }

    @Test
    void whenCreateProducer_thenCreateProducer() {
        val producerCreationDto = ProducerFixtures.getProducerCreationDtoFixture();
        val producer = new Producer();

        Mockito.when(producerMapper.mapToProducer(producerCreationDto)).thenReturn(producer);

        producerService.createProducer(producerCreationDto);

        Mockito.verify(producerRepository, Mockito.times(1)).save(producer);
    }

    @Test
    void givenNonExistentProducer_whenUpdateProducer_thenNotFoundException() {
        val producerId = 1L;
        val producerUpdateDto = ProducerFixtures.getProducerUpdateDtoFixture();

        Mockito.when(producerRepository.findById(producerId)).thenReturn(Optional.empty());

        val exc = Assertions.assertThrows(NotFoundException.class, () -> producerService.updateProducer(producerId, producerUpdateDto));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Producer with ID " + producerId + " not found", exc.getMessage());
    }

    @Test
    void whenUpdateProducer_thenUpdateProducer() {
        val producerId = 1L;
        val producerUpdateDto = ProducerFixtures.getProducerUpdateDtoFixture();
        val producer = new Producer();

        Mockito.when(producerRepository.findById(producerId)).thenReturn(Optional.of(producer));

        producerService.updateProducer(producerId, producerUpdateDto);

        Mockito.verify(producerMapper, Mockito.times(1)).mergeToProducer(producer, producerUpdateDto);
    }

    @Test
    void givenNonExistentProducer_whenDeleteProducer_thenNotFoundException() {
        val producerId = 1L;

        Mockito.when(producerRepository.findById(producerId)).thenReturn(Optional.empty());

        val exc = Assertions.assertThrows(NotFoundException.class, () -> producerService.deleteProducer(producerId));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Producer with ID " + producerId + " not found", exc.getMessage());
    }

    @Test
    void givenProducerWithIngredients_whenDeleteProducer_thenForbiddenException() {
        val producerId = 1L;
        val producer = new Producer();
        producer.setIngredients(List.of(new Ingredient()));

        Mockito.when(producerRepository.findById(producerId)).thenReturn(Optional.of(producer));

        val exc = Assertions.assertThrows(ForbiddenException.class, () -> producerService.deleteProducer(producerId));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Producer with ID " + producerId + " has dependencies and cannot be deleted", exc.getMessage());
    }

    @Test
    void whenDeleteProducer_thenDeleteProducer() {
        val producerId = 1L;
        val producer = new Producer();
        producer.setIngredients(List.of());

        Mockito.when(producerRepository.findById(producerId)).thenReturn(Optional.of(producer));

        Assertions.assertDoesNotThrow(() -> producerService.deleteProducer(producerId));

        Mockito.verify(producerRepository, Mockito.times(1)).delete(producer);
    }
}
