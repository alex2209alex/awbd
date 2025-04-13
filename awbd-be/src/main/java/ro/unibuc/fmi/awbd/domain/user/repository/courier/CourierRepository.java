package ro.unibuc.fmi.awbd.domain.user.repository.courier;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unibuc.fmi.awbd.domain.user.model.courier.Courier;

public interface CourierRepository extends JpaRepository<Courier, Long> {
}
