package ro.unibuc.fmi.awbd.service.cook.mapper;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.unibuc.fmi.awbd.common.exception.InternalServerErrorException;
import ro.unibuc.fmi.awbd.domain.user.model.Role;
import ro.unibuc.fmi.awbd.fixtures.CookFixtures;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest(classes = {CookMapperImpl.class})
class CookMapperTest {
    @Autowired
    private CookMapper cookMapper;

    @Test
    void testMapToCookFilter() {
        Assertions.assertNull(cookMapper.mapToCookFilter(null, null));

        val email = "email";
        val name = "name";

        var cookFilter = cookMapper.mapToCookFilter(email, name);

        Assertions.assertNotNull(cookFilter);
        Assertions.assertEquals(email, cookFilter.getEmail());
        Assertions.assertEquals(name, cookFilter.getName());

        cookFilter = cookMapper.mapToCookFilter(null, name);

        Assertions.assertNotNull(cookFilter);
        Assertions.assertNull(cookFilter.getEmail());
        Assertions.assertEquals(name, cookFilter.getName());

        cookFilter = cookMapper.mapToCookFilter(email, null);

        Assertions.assertNotNull(cookFilter);
        Assertions.assertEquals(email, cookFilter.getEmail());
        Assertions.assertNull(cookFilter.getName());
    }

    @Test
    void testMapToCooksPageDto() {
        Assertions.assertNull(cookMapper.mapToCooksPageDto(null));

        val page = CookFixtures.getPageOfCookPageElementDetailsFixture();

        var cooksPageDto = cookMapper.mapToCooksPageDto(page);

        Assertions.assertNotNull(cooksPageDto);

        Assertions.assertNotNull(cooksPageDto.getPagination());
        Assertions.assertEquals(cooksPageDto.getPagination().getPage(), page.getPaginationInformation().getPage());
        Assertions.assertEquals(cooksPageDto.getPagination().getPageSize(), page.getPaginationInformation().getPageSize());
        Assertions.assertEquals(cooksPageDto.getPagination().getPagesTotal(), page.getPaginationInformation().getPagesTotal());
        Assertions.assertEquals(cooksPageDto.getPagination().getHasNextPage(), page.getPaginationInformation().isHasNextPage());
        Assertions.assertEquals(cooksPageDto.getPagination().getItemsTotal(), page.getPaginationInformation().getItemsTotal());
        Assertions.assertEquals(cooksPageDto.getPagination().getSort(), page.getPaginationInformation().getSort());

        Assertions.assertNotNull(cooksPageDto.getItems());
        Assertions.assertEquals(page.getItems().size(), cooksPageDto.getItems().size());
        for (int i = 0; i < cooksPageDto.getItems().size(); i++) {
            Assertions.assertEquals(page.getItems().get(i).getId(), cooksPageDto.getItems().get(i).getId());
            Assertions.assertEquals(page.getItems().get(i).getEmail(), cooksPageDto.getItems().get(i).getEmail());
            Assertions.assertEquals(page.getItems().get(i).getName(), cooksPageDto.getItems().get(i).getName());
            Assertions.assertEquals(page.getItems().get(i).getSalary(), cooksPageDto.getItems().get(i).getSalary());
        }
    }

    @Test
    void testMapToCookDetailsDto() {
        Assertions.assertNull(cookMapper.mapToCookDetailsDto(null));

        val cook = CookFixtures.getCookFixture();

        val cookDetailsDto = cookMapper.mapToCookDetailsDto(cook);

        Assertions.assertNotNull(cookDetailsDto);
        Assertions.assertEquals(cook.getId(), cookDetailsDto.getId());
        Assertions.assertEquals(cook.getEmail(), cookDetailsDto.getEmail());
        Assertions.assertEquals(cook.getName(), cookDetailsDto.getName());
        Assertions.assertEquals(cook.getSalary(), cookDetailsDto.getSalary());
        Assertions.assertEquals(cook.getProducts().size(), cookDetailsDto.getProducts().size());
        for (int i = 0; i < cook.getProducts().size(); i++) {
            Assertions.assertEquals(cook.getProducts().get(i).getId(), cookDetailsDto.getProducts().get(i).getId());
            Assertions.assertEquals(cook.getProducts().get(i).getName(), cookDetailsDto.getProducts().get(i).getName());
        }
    }

    @Test
    void testMapToCook() {
        Assertions.assertNull(cookMapper.mapToCook(null));

        val cookCreationDto = CookFixtures.getCookCreationDtoFixture();

        val cook = cookMapper.mapToCook(cookCreationDto);

        Assertions.assertNotNull(cook);
        Assertions.assertEquals(cookCreationDto.getEmail(), cook.getEmail());
        Assertions.assertNotNull(cook.getPassword());
        Assertions.assertEquals(cookCreationDto.getName(), cook.getName());
        Assertions.assertEquals(Role.COOK, cook.getRole());
        Assertions.assertEquals(cookCreationDto.getSalary(), cook.getSalary());
    }

    @Test
    void testMergeToCook() {
        val cook = CookFixtures.getCookFixture();

        Assertions.assertDoesNotThrow(() -> cookMapper.mergeToCook(cook, null));

        val oldPassword = cook.getPassword();
        val cookUpdateDto = CookFixtures.getCookUpdateDtoFixture();

        cookMapper.mergeToCook(cook, cookUpdateDto);

        Assertions.assertNotEquals(cook.getPassword(), oldPassword);
        Assertions.assertEquals(cook.getName(), cookUpdateDto.getName());
        Assertions.assertEquals(cook.getSalary(), cookUpdateDto.getSalary());
    }

    @Test
    void givenErrorWithHashingAlgorithm_whenHashPassword_thenInternalServerErrorException() {
        try (MockedStatic<MessageDigest> mocked = mockStatic(MessageDigest.class)) {
            mocked.when(() -> MessageDigest.getInstance(any()))
                    .thenThrow(new NoSuchAlgorithmException());

            val exc = Assertions.assertThrows(InternalServerErrorException.class, () -> cookMapper.hashPassword(""));

            Assertions.assertNotNull(exc);
            Assertions.assertEquals("Error with hashing algorithm", exc.getMessage());
        }
    }
}
