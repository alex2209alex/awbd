package ro.unibuc.fmi.awbd.domain.user.model.restaurantadmin;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ro.unibuc.fmi.awbd.domain.user.model.User;

@Entity
@DiscriminatorValue("RESTAURANT_ADMIN")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class RestaurantAdmin extends User {
}
