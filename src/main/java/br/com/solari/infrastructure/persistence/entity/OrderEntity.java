package br.com.solari.infrastructure.persistence.entity;

import br.com.solari.application.domain.Orderstatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderEntity {

  @Id private String id;

  @Enumerated(EnumType.STRING)
  private Orderstatus orderStatus;

  @ElementCollection
  @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
  @MapKeyColumn(name = "product_id")
  @Column(name = "quantity")
  private Map<String, Integer> products;

  @Column(name = "cpf", nullable = false)
  private String cpf;

  @Column(name = "payment_type", nullable = false)
  private String paymentType;

  @Column(name = "payment_identification", nullable = false)
  private String paymentIdentification;

  @Column(name = "created_date", nullable = false)
  private LocalDateTime createdDate;

  @Column(name = "last_modified_date", nullable = false)
  private LocalDateTime lastModifiedDate;
}
