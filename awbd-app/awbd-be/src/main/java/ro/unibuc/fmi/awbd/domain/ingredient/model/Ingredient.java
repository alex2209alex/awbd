package ro.unibuc.fmi.awbd.domain.ingredient.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.unibuc.fmi.awbd.domain.producer.model.Producer;

import java.util.List;

@Entity
@Table(name = "ingredients")
@Data
@NoArgsConstructor
public class Ingredient {
    @Id
    @SequenceGenerator(name = "ingredients_gen", sequenceName = "ingredients_seq", allocationSize = 20)
    @GeneratedValue(generator = "ingredients_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "calories", nullable = false)
    private Double calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producer_id", nullable = false)
    private Producer producer;

    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY)
    private List<IngredientProductAssociation> ingredientProductAssociations;
}
