package com.devsu.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class ClientDTO extends PersonDTO {

    @NotEmpty(message = "{clientId.empty}")
    private String clientId;

    @NotEmpty(message = "{password.empty}")
    @Size(max = 25, message = "{password.size.max}")
    private String password;

    private Boolean status;
}
