package ro.unibuc.fmi.awbd.domain.user.model.cook;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ro.unibuc.fmi.awbd.domain.product.model.Product;
import ro.unibuc.fmi.awbd.domain.user.model.User;

import java.util.List;

@Entity
@DiscriminatorValue("COOK")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Cook extends User {
    @Column(name = "salary", nullable = false)
    private Double salary;

    @ManyToMany
    @JoinTable(
            name = "cook_product_associations",
            joinColumns = @JoinColumn(name = "cook_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "product_id", nullable = false)
    )
    List<Product> products;
}
