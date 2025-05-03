package ro.unibuc.fmi.awbd.domain.user.repository.restaurantadmin;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unibuc.fmi.awbd.domain.user.model.restaurantadmin.RestaurantAdmin;

public interface RestaurantAdminRepository extends JpaRepository<RestaurantAdmin, Long> {
}
