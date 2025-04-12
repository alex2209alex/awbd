package ro.unibuc.fmi.awbd.domain.producer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unibuc.fmi.awbd.domain.producer.model.Producer;

import java.util.List;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
    List<Producer> findAllByOrderByName();
}
