package ro.unibuc.fmi.awbd.common.model;

import lombok.Builder;
import lombok.Data;
import lombok.val;

@Data
@Builder
public class PaginationInformation {
    private int page;
    private int pageSize;
    private long pagesTotal;
    private boolean hasNextPage;
    private long itemsTotal;
    private String sort;

    public static PaginationInformation of(long itemsTotal, PaginationRequest paginationRequest) {
        val pagesTotal = (long) Math.ceil((double) itemsTotal / paginationRequest.getPageSize());

        return PaginationInformation.builder()
                .page(paginationRequest.getPage())
                .pageSize(paginationRequest.getPageSize())
                .pagesTotal(pagesTotal)
                .hasNextPage(pagesTotal > paginationRequest.getPage())
                .itemsTotal(itemsTotal)
                .sort(paginationRequest.getSort())
                .build();
    }
}
