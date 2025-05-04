package ro.unibuc.fmi.awbd.domain.user.model.courier;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ro.unibuc.fmi.awbd.domain.onlineorder.model.OnlineOrder;
import ro.unibuc.fmi.awbd.domain.user.model.User;

import java.util.List;

@Entity
@DiscriminatorValue("COURIER")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Courier extends User {
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "salary", nullable = false)
    private Double salary;

    @OneToMany(mappedBy = "courier", fetch = FetchType.LAZY)
    private List<OnlineOrder> onlineOrders;
}
