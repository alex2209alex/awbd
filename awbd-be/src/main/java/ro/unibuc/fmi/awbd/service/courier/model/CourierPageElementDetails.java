package ro.unibuc.fmi.awbd.service.courier.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CourierPageElementDetails {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private Double salary;
}
