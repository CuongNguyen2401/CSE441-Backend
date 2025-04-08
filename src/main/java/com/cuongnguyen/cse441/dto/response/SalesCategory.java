package com.cuongnguyen.cse441.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesCategory {

    String categoryName;
    Double totalRevenue;
}
