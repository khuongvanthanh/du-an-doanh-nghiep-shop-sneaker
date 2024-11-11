package com.example.dto.response.bills;

import lombok.Builder;
import lombok.Getter;
import com.example.model.Customer;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Builder
public class BillListResponse {
    private Long id;
    private String code;
    private Customer customer;
    private BigDecimal total;
    private Integer billStatus;
    private Date createAt;
}
