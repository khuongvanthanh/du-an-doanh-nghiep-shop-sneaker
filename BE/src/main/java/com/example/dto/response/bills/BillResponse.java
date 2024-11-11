package com.example.dto.response.bills;

import lombok.Builder;
import lombok.Getter;
import com.example.model.Customer;

import java.util.List;

@Getter
@Builder
public class BillResponse {
    private Long id;
    private String code;
    private Integer billStatus;
    private Customer customer;
    private BillCouponResponse coupon;
    private List<BillDetailResponse> billDetails;
}
