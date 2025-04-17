package ro.unibuc.fmi.awbd.common.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class PageRequest<FILTER> {
    private final FILTER filter;
    private final PaginationRequest pagination;
}
