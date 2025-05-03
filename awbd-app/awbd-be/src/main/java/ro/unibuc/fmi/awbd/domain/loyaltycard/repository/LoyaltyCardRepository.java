package ro.unibuc.fmi.awbd.domain.loyaltycard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unibuc.fmi.awbd.domain.loyaltycard.model.LoyaltyCard;

public interface LoyaltyCardRepository extends JpaRepository<LoyaltyCard, Long> {
}
