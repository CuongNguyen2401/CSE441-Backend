package com.thanhtan.groceryshop.dto.request;


import com.thanhtan.groceryshop.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;


@Value
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOrderRequest implements Serializable {
    Long id;
    @NotBlank(message = "FILL_IN_THE_INPUT_FIELD")
    String customerName;
    @NotBlank(message = "FILL_IN_THE_INPUT_FIELD")
    String email;
    String phoneNumber;
    @NotBlank(message = "FILL_IN_THE_INPUT_FIELD")
    String address;
    String note;
    OrderStatus orderStatus;

    List<OrderItemRequest> orderItems;

}