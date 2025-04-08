package com.cuongnguyen.cse441.entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Transaction extends BaseEntity{

     String transactionId;
     String username;
     Long orderId;
}
