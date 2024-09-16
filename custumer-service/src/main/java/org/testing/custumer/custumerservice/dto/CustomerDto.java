package org.testing.custumer.custumerservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString @Builder
public class CustomerDto {

    private Long id;

    @NotEmpty @Size( min = 2 )
    private String firstName;

    @NotEmpty @Size( min = 2 )
    private String lastName;

    @NotEmpty @Size( min = 8 )
    private String email;
}
