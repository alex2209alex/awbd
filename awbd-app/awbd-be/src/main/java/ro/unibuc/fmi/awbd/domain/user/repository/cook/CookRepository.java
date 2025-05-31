package ro.unibuc.fmi.awbd.domain.user.repository.cook;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unibuc.fmi.awbd.domain.user.model.cook.Cook;

import java.util.Optional;

public interface CookRepository extends JpaRepository<Cook, Long> {
    Optional<Cook> findByEmail(String email);
}
