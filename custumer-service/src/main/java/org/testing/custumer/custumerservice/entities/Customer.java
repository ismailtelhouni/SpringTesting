package org.testing.custumer.custumerservice.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "customer")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @ToString @Builder
public class Customer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size( min = 2 )
    private String firstName;

    @NotEmpty
    @Size( min = 2 )
    private String lastName;

    @NotEmpty
    @Size( min = 8 )
    private String email;

}
