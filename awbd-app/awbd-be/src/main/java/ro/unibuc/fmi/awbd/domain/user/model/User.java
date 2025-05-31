package ro.unibuc.fmi.awbd.domain.user.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
public class User {
    @Id
    @SequenceGenerator(name = "users_gen", sequenceName = "users_seq", allocationSize = 20)
    @GeneratedValue(generator = "users_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(STRING)
    @Column(name = "role", insertable = false, updatable = false)
    private Role role;
}
