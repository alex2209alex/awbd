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

    @InjectMocks
    ProducerService producerService;

    @Test
    void whenGetProducersPage_thenGetProducersPage() {
        val pageRequest = ProducerFixtures.getPageRequestFixture();
        val page = ProducerFixtures.getPageOfProducerPageElementDetailsDtoFixture();
        val producerPageDto = new ProducersPageDto();

        Mockito.when(producerSearchRepository.getProducersPage(pageRequest)).thenReturn(page);
        Mockito.when(producerMapper.mapToProducersPageDto(page)).thenReturn(producerPageDto);

        Assertions.assertEquals(producerPageDto, producerService.getProducersPage(pageRequest));
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
        val id = 1L;

        Mockito.when(producerRepository.findById(id)).thenReturn(Optional.empty());

        val exc = Assertions.assertThrows(NotFoundException.class, () -> producerService.getProducerDetails(id));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Producer with ID " + id + " not found", exc.getMessage());
    }

    @Test
    void whenGetProducerDetails_thenGetProducerDetails() {
        val id = 1L;
        val producer = new Producer();
        val producerDetailsDto = new ProducerDetailsDto();

        Mockito.when(producerRepository.findById(id)).thenReturn(Optional.of(producer));
        Mockito.when(producerMapper.mapToProducerDetailsDto(producer)).thenReturn(producerDetailsDto);

        Assertions.assertEquals(producerDetailsDto, producerService.getProducerDetails(id));
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
        val id = 1L;
        val producerUpdateDto = ProducerFixtures.getProducerUpdateDtoFixture();

        Mockito.when(producerRepository.findById(id)).thenReturn(Optional.empty());

        val exc = Assertions.assertThrows(NotFoundException.class, () -> producerService.updateProducer(id, producerUpdateDto));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Producer with ID " + id + " not found", exc.getMessage());
    }

    @Test
    void whenUpdateProducer_thenUpdateProducer() {
        val id = 1L;
        val producerUpdateDto = ProducerFixtures.getProducerUpdateDtoFixture();
        val producer = new Producer();

        Mockito.when(producerRepository.findById(id)).thenReturn(Optional.of(producer));

        producerService.updateProducer(id, producerUpdateDto);

        Mockito.verify(producerMapper, Mockito.times(1)).mergeToProducer(producer, producerUpdateDto);
    }

    @Test
    void givenNonExistentProducer_whenDeleteProducer_thenNotFoundException() {
        val id = 1L;

        Mockito.when(producerRepository.findById(id)).thenReturn(Optional.empty());

        val exc = Assertions.assertThrows(NotFoundException.class, () -> producerService.deleteProducer(id));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Producer with ID " + id + " not found", exc.getMessage());
    }

    @Test
    void givenProducerWithIngredients_whenDeleteProducer_thenForbiddenException() {
        val id = 1L;
        val producer = new Producer();
        producer.setIngredients(List.of(new Ingredient()));

        Mockito.when(producerRepository.findById(id)).thenReturn(Optional.of(producer));

        val exc = Assertions.assertThrows(ForbiddenException.class, () -> producerService.deleteProducer(id));

        Assertions.assertNotNull(exc);
        Assertions.assertEquals("Producer with ID " + id + " has dependencies and cannot be deleted", exc.getMessage());
    }

    @Test
    void whenDeleteProducer_thenDeleteProducer() {
        val id = 1L;
        val producer = new Producer();
        producer.setIngredients(List.of());

        Mockito.when(producerRepository.findById(id)).thenReturn(Optional.of(producer));

        producerService.deleteProducer(id);

        Mockito.verify(producerRepository, Mockito.times(1)).delete(producer);
    }
}
