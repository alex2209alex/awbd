package ro.unibuc.fmi.awbd.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {
    private long page;
    private int pageSize;
    private String sort;
}
