package com.example.dto.response.bills;

import lombok.Builder;
import lombok.Getter;
import com.example.enums.PaymentMethod;
import com.example.model.Coupon;
import com.example.model.Customer;
import com.example.model.Employee;
import com.example.model.User;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Builder
public class BillEditResponse {
    private Long id;
    private String code;
    private Customer customer;
    private BillCouponResponse coupon;
    private Integer status;
    private BigDecimal shipping;
    private BigDecimal subtotal;
    private BigDecimal sellerDiscount;
    private BigDecimal total;
    private PaymentMethod paymentMethod;
    private String message;
    private String note;
    private Date paymentTime;
    private Employee employee;
    private List<BillDetailResponse> billDetails;
}
