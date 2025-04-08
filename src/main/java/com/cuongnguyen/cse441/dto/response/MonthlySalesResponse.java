package com.cuongnguyen.cse441.dto.response;

import com.cuongnguyen.cse441.enums.Month;
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
