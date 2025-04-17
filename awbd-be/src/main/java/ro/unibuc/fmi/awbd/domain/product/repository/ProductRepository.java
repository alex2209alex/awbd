package ro.unibuc.fmi.awbd.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unibuc.fmi.awbd.domain.product.model.Product;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByOrderByName();

    List<Product> findAllByIdIn(Set<Long> ids);
}
