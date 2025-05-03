package ro.unibuc.fmi.awbd.domain.producer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unibuc.fmi.awbd.domain.producer.model.Producer;

import java.util.List;
import java.util.Optional;

public interface ProducerRepository extends JpaRepository<Producer, Long> {
    List<Producer> findAllByOrderByName();

    Optional<Producer> findByNameAndAddress(String name, String address);
}
