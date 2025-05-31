package ro.unibuc.fmi.awbd.domain.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOnlineOrderAssociationId {
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "online_order_id", nullable = false)
    private Long onlineOrderId;
}
