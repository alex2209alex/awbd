package ro.unibuc.fmi.awbd.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unibuc.fmi.awbd.domain.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
