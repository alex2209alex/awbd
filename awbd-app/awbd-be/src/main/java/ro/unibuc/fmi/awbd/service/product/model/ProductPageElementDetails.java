package ro.unibuc.fmi.awbd.service.product.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductPageElementDetails {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private Double calories;
}
