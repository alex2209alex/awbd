package ro.unibuc.fmi.awbd.domain.producer.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.unibuc.fmi.awbd.domain.ingredient.model.Ingredient;

import java.util.List;

@Entity
@Table(name = "producers")
@Data
@NoArgsConstructor
public class Producer {
    @Id
    @SequenceGenerator(name = "producers_gen", sequenceName = "producers_seq", allocationSize = 20)
    @GeneratedValue(generator = "producers_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "producer", fetch = FetchType.LAZY)
    private List<Ingredient> ingredients;
}
