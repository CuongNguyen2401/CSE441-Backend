package com.thanhtan.groceryshop.dto.request;

import com.thanhtan.groceryshop.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCategoryRequest {
    Long id;
    String name;
    String description;
    Status status;
}
