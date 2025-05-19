package br.com.solari.infrastructure.persistence.repository;

import br.com.solari.infrastructure.persistence.entity.OrderEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

  Optional<OrderEntity> findById(final String id);
}
