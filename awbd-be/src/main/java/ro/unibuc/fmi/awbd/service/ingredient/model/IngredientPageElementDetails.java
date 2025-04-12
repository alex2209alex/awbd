package ro.unibuc.fmi.awbd.service.ingredient.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IngredientPageElementDetails {
    private Long id;
    private String name;
    private Double price;
    private Double calories;
    private String producer;
}
