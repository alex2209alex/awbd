package ro.unibuc.fmi.awbd.domain.user.repository.client;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unibuc.fmi.awbd.domain.user.model.client.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
