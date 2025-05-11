package ro.unibuc.fmi.awbd.domain.product.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.unibuc.fmi.awbd.domain.ingredient.model.IngredientProductAssociation;
import ro.unibuc.fmi.awbd.domain.producer.model.ProductOnlineOrderAssociation;
import ro.unibuc.fmi.awbd.domain.user.model.cook.Cook;

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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<IngredientProductAssociation> ingredientProductAssociations = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductOnlineOrderAssociation> productOnlineOrderAssociations;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cook_product_associations",
            joinColumns = @JoinColumn(name = "product_id", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "cook_id", insertable = false, updatable = false)
    )
    List<Cook> cooks = new ArrayList<>();
}
