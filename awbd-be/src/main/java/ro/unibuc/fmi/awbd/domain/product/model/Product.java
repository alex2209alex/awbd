package ro.unibuc.fmi.awbd.domain.product.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.unibuc.fmi.awbd.domain.ingredient.model.IngredientProductAssociation;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
public class Product {
    @Id
    @SequenceGenerator(name = "products_gen", sequenceName = "products_seq", allocationSize = 20)
    @GeneratedValue(generator = "products_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<IngredientProductAssociation> ingredientProductAssociations = new ArrayList<>();
}
