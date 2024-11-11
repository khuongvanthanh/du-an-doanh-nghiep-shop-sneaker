package com.example.dto.requests.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.example.enums.TodoDiscountType;
import com.example.enums.TodoType;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class BillCouponFilter {
        private String keyword;
        private int pageNo = 1;
        private int pageSize = 5;
        private BigDecimal subtotal ;
}
