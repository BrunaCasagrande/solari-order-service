package br.com.solari.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InventoryDTO {

    private Integer id;

    private String sku;

    private Integer quantity;
}
