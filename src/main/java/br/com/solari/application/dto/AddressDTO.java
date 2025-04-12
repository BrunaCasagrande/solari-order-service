package br.com.solari.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

    private Integer id;
    private String street;
    private String number;
    private String city;
    private String state;
    private String zipCode;
}
