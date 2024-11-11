package com.example.service;

import com.example.dto.requests.CouponImageReq;
import com.example.dto.requests.CouponRequest;
import com.example.dto.requests.common.BillCouponFilter;
import com.example.dto.requests.common.CouponParamFilter;
import com.example.dto.response.CouponCustomerResponse;
import com.example.dto.response.CouponResponse;
import com.example.dto.response.PageableResponse;
import com.example.model.Coupon;
import com.example.model.Customer;

import java.util.List;

public interface CouponService {
    PageableResponse getAllCoupon(CouponParamFilter param);
    PageableResponse getAllCouponCustomer(Long customerId,BillCouponFilter param);
    PageableResponse getAllCouponCustomerGood(Long customerId,BillCouponFilter param);
    CouponResponse getCouponById(Long id);
    long storeCoupon(CouponRequest couponRequest);
    void storeCouponImages(CouponImageReq req);
    long updateCoupon(Long id, CouponRequest couponRequest);
    void deleteCoupon(Long id);
    void deleteCouponImage(Long couponId);
    void sendCouponEmail(Coupon coupon, Customer customer);
    Coupon findCouponById(Long id);
    Customer findCustomerById(Long id);
}
