package ro.unibuc.fmi.awbd.fixtures;

import ro.unibuc.fmi.awbd.common.model.PaginationInformation;

public class PaginationInformationFixtures {
    private PaginationInformationFixtures() {
    }

    public static PaginationInformation getPaginationInformationFixture() {
        return PaginationInformation.builder()
                .page(1)
                .pageSize(2)
                .pagesTotal(3)
                .hasNextPage(true)
                .itemsTotal(5)
                .sort("+sort")
                .build();
    }
}
