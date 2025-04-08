package com.cuongnguyen.cse441.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;


@Value
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemRequest implements Serializable {
    Long productId;
    int quantity;
    double price;
}