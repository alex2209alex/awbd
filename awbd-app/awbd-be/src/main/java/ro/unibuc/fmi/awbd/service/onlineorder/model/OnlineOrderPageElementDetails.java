package ro.unibuc.fmi.awbd.service.onlineorder.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OnlineOrderPageElementDetails {
    private Long id;
    private String client;
    private String courier;
    private String status;
    private Double price;
}
