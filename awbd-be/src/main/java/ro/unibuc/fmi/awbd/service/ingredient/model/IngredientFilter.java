package ro.unibuc.fmi.awbd.service.ingredient.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientFilter {
    private String name;
    private String producer;
}
