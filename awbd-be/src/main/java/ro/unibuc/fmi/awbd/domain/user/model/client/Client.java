package ro.unibuc.fmi.awbd.domain.user.model.client;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ro.unibuc.fmi.awbd.domain.loyaltycard.model.LoyaltyCard;
import ro.unibuc.fmi.awbd.domain.user.model.User;

@Entity
@DiscriminatorValue("CLIENT")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Client extends User {
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loyalty_card_id", referencedColumnName = "id")
    private LoyaltyCard loyaltyCard;
}
