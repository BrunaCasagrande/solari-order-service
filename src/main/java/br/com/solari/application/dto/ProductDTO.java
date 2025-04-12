package br.com.solari.application.dto;

import lombok.*;

import java.math.BigDecimal;

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
