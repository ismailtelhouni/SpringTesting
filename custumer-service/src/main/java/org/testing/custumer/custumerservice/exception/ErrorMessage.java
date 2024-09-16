package org.testing.custumer.custumerservice.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorMessage {
    String message;
    Date timestamp;
    Integer code;
}
