package ro.unibuc.fmi.awbd.domain.ingredient.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientProductAssociationId {
    @Column(name = "ingredient_id", nullable = false)
    private Long ingredientId;

    @Column(name = "product_id", nullable = false)
    private Long productId;
}
