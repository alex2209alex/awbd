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
    @Column(name = "id")
    private Long id;

    @Column(name = "points", nullable = false)
    private Long points;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Client client;
}
