package com.thanhtan.groceryshop.dto.request;

import com.thanhtan.groceryshop.enums.Status;
import com.thanhtan.groceryshop.validator.DobConstraint;
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

