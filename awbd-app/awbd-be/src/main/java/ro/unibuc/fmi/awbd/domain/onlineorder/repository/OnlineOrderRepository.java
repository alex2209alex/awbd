package ro.unibuc.fmi.awbd.domain.onlineorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unibuc.fmi.awbd.domain.onlineorder.model.OnlineOrder;

public interface OnlineOrderRepository extends JpaRepository<OnlineOrder, Long> {
}
