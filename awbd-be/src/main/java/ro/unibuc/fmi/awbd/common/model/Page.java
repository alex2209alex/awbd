package ro.unibuc.fmi.awbd.common.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Page<T> {
    List<T> items;
    PaginationInformation paginationInformation;
}
