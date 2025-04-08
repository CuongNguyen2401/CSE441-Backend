package com.cuongnguyen.cse441.dto.request;

import com.cuongnguyen.cse441.enums.Status;
import com.cuongnguyen.cse441.validator.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
    String firstName;
    String lastName;
    String phoneNumber;
    String gender;
    String address;
    String email;
    Status status;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dateOfBirth;
}

