package br.com.solari.application.dto;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProductDTO {

  private Integer id;

  private String name;

  private String description;

  private String sku;

  private BigDecimal price;
}
