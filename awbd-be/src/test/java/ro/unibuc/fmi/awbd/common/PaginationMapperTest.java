package ro.unibuc.fmi.awbd.common;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ro.unibuc.fmi.awbd.common.mapper.PaginationMapper;
import ro.unibuc.fmi.awbd.common.mapper.PaginationMapperImpl;

@SpringBootTest(classes = {PaginationMapperImpl.class})
class PaginationMapperTest {
    @Autowired
    private PaginationMapper paginationMapper;

    @Test
    void testMapToPaginationRequest() {
        val page = 1L;
        val pageSize = 2;
        val sort = "+sort";

        val paginationRequest = paginationMapper.mapToPaginationRequest(page, pageSize, sort);

        Assertions.assertNotNull(paginationRequest);
        Assertions.assertEquals(page, paginationRequest.getPage());
        Assertions.assertEquals(pageSize, paginationRequest.getPageSize());
        Assertions.assertNotNull(paginationRequest.getSort());
    }
}
