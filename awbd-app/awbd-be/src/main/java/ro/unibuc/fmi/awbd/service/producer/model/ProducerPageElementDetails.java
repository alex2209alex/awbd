package ro.unibuc.fmi.awbd.service.producer.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProducerPageElementDetails {
    private Long id;
    private String name;
    private String address;
}
