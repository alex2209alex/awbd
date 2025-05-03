package ro.unibuc.fmi.awbd.service.courier.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourierFilter {
    private String email;
    private String name;
    private String phoneNumber;
}
