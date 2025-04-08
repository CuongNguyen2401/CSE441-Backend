package com.cuongnguyen.cse441.dto.response;

import com.cuongnguyen.cse441.entity.OrderItem;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * DTO for {@link OrderItem}
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