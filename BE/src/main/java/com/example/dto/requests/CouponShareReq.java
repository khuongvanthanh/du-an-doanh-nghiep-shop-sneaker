package com.example.dto.requests;

import lombok.Getter;
import lombok.Setter;
import com.example.model.Coupon;
import com.example.model.Customer;

@Getter
@Setter
public class CouponShareReq {
    private Long id;
    private Coupon coupon;
    private Customer customer;
    private Boolean isDeleted;
}
