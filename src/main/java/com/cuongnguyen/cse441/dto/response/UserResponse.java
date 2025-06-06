package com.cuongnguyen.cse441.dto.response;


import com.cuongnguyen.cse441.enums.Gender;
import com.cuongnguyen.cse441.enums.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
public class UserResponse extends BaseDTO {

    String username;
    String firstName;
    String lastName;
    Date dateOfBirth;
    Gender gender;
    String phoneNumber;
    String address;
    String email;
    String avatar;
    Set<String> roles;
    Status status;

}