package com.cuongnguyen.cse441.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Version;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class RefreshToken extends BaseEntity  {


    @Column(length = 1000)
    String token;
    String username;
    Date expiryTime;
    @Version
     Integer version;

}
