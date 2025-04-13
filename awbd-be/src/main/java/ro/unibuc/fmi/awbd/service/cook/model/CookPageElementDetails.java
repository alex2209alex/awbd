package ro.unibuc.fmi.awbd.service.cook.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CookPageElementDetails {
    private Long id;
    private String email;
    private String name;
    private Double salary;
}
