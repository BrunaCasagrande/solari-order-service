package br.com.solari.infrastructure.presenter.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ClientDTO {

    int id;
    String name;
    String cpf;
    String phoneNumber;
    String email;
    AddressDTO address;
}
