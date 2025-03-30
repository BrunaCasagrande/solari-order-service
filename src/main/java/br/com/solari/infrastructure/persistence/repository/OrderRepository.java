package br.com.solari.infrastructure.persistence.repository;

import br.com.solari.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

	Optional<OrderEntity> findById(final String id);

}
