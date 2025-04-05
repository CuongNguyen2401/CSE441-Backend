package com.thanhtan.groceryshop.dto.response;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.thanhtan.groceryshop.entity.OrderItem}
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse implements Serializable {
    String productName;
    String image;
    int quantity;
    double price;

}