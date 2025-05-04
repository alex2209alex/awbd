package ro.unibuc.fmi.awbd.domain.onlineorder.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ro.unibuc.fmi.awbd.domain.producer.model.ProductOnlineOrderAssociation;
import ro.unibuc.fmi.awbd.domain.user.model.client.Client;
import ro.unibuc.fmi.awbd.domain.user.model.courier.Courier;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "online_orders")
@Data
@NoArgsConstructor
public class OnlineOrder {
    @Id
    @SequenceGenerator(name = "online_orders_gen", sequenceName = "online_orders_seq", allocationSize = 20)
    @GeneratedValue(generator = "online_orders_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "creation_time", nullable = false)
    private Instant creationTime;

    @Enumerated(STRING)
    @Column(name = "online_order_status", nullable = false, insertable = false, updatable = false)
    private OnlineOrderStatus onlineOrderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @OneToMany(mappedBy = "onlineOrder", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<ProductOnlineOrderAssociation> productOnlineOrderAssociations = new ArrayList<>();
}
