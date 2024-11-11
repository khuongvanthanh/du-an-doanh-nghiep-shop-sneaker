package com.example.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import com.example.enums.TodoDiscountType;
import com.example.enums.TodoType;
import com.example.model.Customer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Builder
public class CouponCustomerResponse {
    private Long id;
    private String code;
    private String name;
    private TodoType type;
    private BigDecimal discountValue;
    private TodoDiscountType discountType;
    private BigDecimal maxValue;
    private Integer quantity;
    private Integer usageCount;
    private BigDecimal conditions;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private Date endDate;
    private String status;
    private String description;
    private String imageUrl;
    private List<Long> customers;
}
