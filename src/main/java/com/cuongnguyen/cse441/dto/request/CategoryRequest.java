package com.cuongnguyen.cse441.dto.request;

import com.cuongnguyen.cse441.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {
    String name;
    String description;
    @Builder.Default
    Status status = Status.ACTIVE;
}
