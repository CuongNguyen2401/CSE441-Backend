package com.thanhtan.groceryshop.dto.response;

import com.thanhtan.groceryshop.enums.Month;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthlySalesResponse {
     Month month;
     double sales;
}
