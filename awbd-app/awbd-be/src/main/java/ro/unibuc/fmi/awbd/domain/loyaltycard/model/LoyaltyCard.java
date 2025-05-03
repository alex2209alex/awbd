package ro.unibuc.fmi.awbd.domain.loyaltycard.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.unibuc.fmi.awbd.domain.user.model.client.Client;

@Entity
@Table(name = "loyalty_cards")
@Data
@NoArgsConstructor
public class LoyaltyCard {
    @Id
    @SequenceGenerator(name = "loyalty_cards_gen", sequenceName = "loyalty_cards_seq", allocationSize = 20)
    @GeneratedValue(generator = "loyalty_cards_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "points", nullable = false)
    private Long points;

    @OneToOne(mappedBy = "loyaltyCard")
    private Client client;
}
