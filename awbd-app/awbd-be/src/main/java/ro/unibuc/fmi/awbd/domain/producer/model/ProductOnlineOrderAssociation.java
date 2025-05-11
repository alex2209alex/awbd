package ro.unibuc.fmi.awbd.domain.producer.model;

import jakarta.persistence.*;
import lombok.*;
import ro.unibuc.fmi.awbd.domain.onlineorder.model.OnlineOrder;
import ro.unibuc.fmi.awbd.domain.product.model.Product;

@Entity
@Table(name = "product_online_order_associations")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductOnlineOrderAssociation {
    @EmbeddedId
    private ProductOnlineOrderAssociationId id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @ToString.Exclude
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "online_order_id", insertable = false, updatable = false)
    @ToString.Exclude
    private OnlineOrder onlineOrder;
}
