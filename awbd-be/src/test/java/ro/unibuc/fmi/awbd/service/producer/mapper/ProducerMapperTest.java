package ro.unibuc.fmi.awbd.service.producer.mapper;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.unibuc.fmi.awbd.fixtures.ProducerFixtures;

import java.util.List;

@SpringBootTest(classes = {ProducerMapperImpl.class})
class ProducerMapperTest {
    @Autowired
    private ProducerMapper producerMapper;

    @Test
    void testMapToProducerFilter() {
        val name = "name";
        val address = "address";

        val producerFilter = producerMapper.mapToProducerFilter(name, address);

        Assertions.assertNotNull(producerFilter);
        Assertions.assertEquals(name, producerFilter.getName());
        Assertions.assertEquals(address, producerFilter.getAddress());
    }

    @Test
    void testMapToProducersPageDto() {
        val page = ProducerFixtures.getPageOfProducerPageElementDetailsDtoFixture();

        val producersPageDto = producerMapper.mapToProducersPageDto(page);

        Assertions.assertNotNull(producersPageDto);

        Assertions.assertNotNull(producersPageDto.getPagination());
        Assertions.assertEquals(producersPageDto.getPagination().getPage(), page.getPaginationInformation().getPage());
        Assertions.assertEquals(producersPageDto.getPagination().getPageSize(), page.getPaginationInformation().getPageSize());
        Assertions.assertEquals(producersPageDto.getPagination().getPagesTotal(), page.getPaginationInformation().getPagesTotal());
        Assertions.assertEquals(producersPageDto.getPagination().getHasNextPage(), page.getPaginationInformation().isHasNextPage());
        Assertions.assertEquals(producersPageDto.getPagination().getItemsTotal(), page.getPaginationInformation().getItemsTotal());
        Assertions.assertEquals(producersPageDto.getPagination().getSort(), page.getPaginationInformation().getSort());

        Assertions.assertNotNull(producersPageDto.getItems());
        Assertions.assertEquals(page.getItems().size(), producersPageDto.getItems().size());
        for (int i = 0; i < producersPageDto.getItems().size(); i++) {
            Assertions.assertEquals(page.getItems().get(i).getId(), producersPageDto.getItems().get(i).getId());
            Assertions.assertEquals(page.getItems().get(i).getName(), producersPageDto.getItems().get(i).getName());
            Assertions.assertEquals(page.getItems().get(i).getAddress(), producersPageDto.getItems().get(i).getAddress());
        }
    }

    @Test
    void testMapToProducerSearchDetailsDtos() {
        val producer = ProducerFixtures.getProducerFixture();

        val producerSearchDetailsDtos = producerMapper.mapToProducerSearchDetailsDtos(List.of(producer));

        Assertions.assertNotNull(producerSearchDetailsDtos);
        Assertions.assertEquals(1, producerSearchDetailsDtos.size());
        Assertions.assertEquals(producer.getId(), producerSearchDetailsDtos.getFirst().getId());
        Assertions.assertEquals(producer.getName(), producerSearchDetailsDtos.getFirst().getName());
    }

    @Test
    void testMapToProducerDetailsDto() {
        val producer = ProducerFixtures.getProducerFixture();

        val producerDetailsDto = producerMapper.mapToProducerDetailsDto(producer);

        Assertions.assertNotNull(producerDetailsDto);
        Assertions.assertEquals(producer.getId(), producerDetailsDto.getId());
        Assertions.assertEquals(producer.getName(), producerDetailsDto.getName());
        Assertions.assertEquals(producer.getAddress(), producerDetailsDto.getAddress());
    }

    @Test
    void testMapToProducer() {
        val producerCreationDto = ProducerFixtures.getProducerCreationDtoFixture();

        val producer = producerMapper.mapToProducer(producerCreationDto);

        Assertions.assertNotNull(producer);
        Assertions.assertEquals(producerCreationDto.getName(), producer.getName());
        Assertions.assertEquals(producerCreationDto.getAddress(), producer.getAddress());
    }

    @Test
    void testMergeToProducer() {
        val producer = ProducerFixtures.getProducerFixture();
        producer.setName("old");
        producer.setAddress("old");
        val producerUpdateDto = ProducerFixtures.getProducerUpdateDtoFixture();

        producerMapper.mergeToProducer(producer, producerUpdateDto);

        Assertions.assertEquals(producerUpdateDto.getName(), producer.getName());
        Assertions.assertEquals(producerUpdateDto.getAddress(), producer.getAddress());
    }
}
