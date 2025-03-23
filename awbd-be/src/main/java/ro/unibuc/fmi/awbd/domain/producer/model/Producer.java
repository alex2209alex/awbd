package ro.unibuc.fmi.awbd.domain.producer.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;
}
