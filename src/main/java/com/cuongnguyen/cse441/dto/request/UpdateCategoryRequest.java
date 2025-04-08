package com.cuongnguyen.cse441.dto.request;

import com.cuongnguyen.cse441.enums.Status;
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
