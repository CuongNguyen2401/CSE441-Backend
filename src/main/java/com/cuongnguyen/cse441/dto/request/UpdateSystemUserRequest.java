package com.cuongnguyen.cse441.dto.request;

import com.cuongnguyen.cse441.enums.Gender;
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
public class UpdateSystemUserRequest {

    @Size(min = 8, max = 20, message = "INVALID_PASSWORD")
    String password;
    String firstName;
    String lastName;
    String phoneNumber;
    Gender gender;
    String address;
    String email;
    Status status;
    String roles;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dateOfBirth;
}
