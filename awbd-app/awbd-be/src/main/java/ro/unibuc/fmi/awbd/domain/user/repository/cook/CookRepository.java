package ro.unibuc.fmi.awbd.domain.user.repository.cook;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unibuc.fmi.awbd.domain.user.model.cook.Cook;

public interface CookRepository extends JpaRepository<Cook, Long> {
}
