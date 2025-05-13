package ro.unibuc.fmi.awbd.service.onlineorder.model;

import lombok.*;
import ro.unibuc.fmi.awbd.domain.onlineorder.model.OnlineOrderStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OnlineOrderPageElementDetails {
    private Long id;
    private String address;
    private String client;
    private String courier;
    private String status;
    private Double price;

    public OnlineOrderPageElementDetails(Long id, String address, String client, String courier, OnlineOrderStatus onlineOrderStatus, Double price) {
        this.id = id;
        this.address = address;
        this.client = client;
        this.status = onlineOrderStatus.name();
        this.courier = courier;
        this.price = price;
    }
}
