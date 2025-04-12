package ro.unibuc.fmi.awbd.domain.ingredient.model;

import jakarta.persistence.*;
import lombok.*;
import ro.unibuc.fmi.awbd.domain.product.model.Product;

@Entity
@Table(name = "ingredient_product_associations")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class IngredientProductAssociation {
    @EmbeddedId
    private IngredientProductAssociationId id;

    @Column(name = "quantity")
    private Double quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", insertable = false, updatable = false)
    @ToString.Exclude
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @ToString.Exclude
    private Product product;
}
