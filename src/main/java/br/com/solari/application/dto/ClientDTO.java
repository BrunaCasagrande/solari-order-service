package br.com.solari.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ClientDTO {

    private Integer id;

    private String name;

    private String cpf;

    private String phoneNumber;

    private String email;

    private AddressDTO address;
}
