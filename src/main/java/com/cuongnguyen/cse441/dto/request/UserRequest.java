package com.cuongnguyen.cse441.dto.request;

import com.cuongnguyen.cse441.enums.Status;
import com.cuongnguyen.cse441.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @Size(min = 8, max = 20, message = "USERNAME_INVALID")
    String username;
    @Size(min = 8, max = 20, message = "INVALID_PASSWORD")
    String password;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String gender;
    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dateOfBirth;
    @Builder.Default
    Status status = Status.ACTIVE;
}
