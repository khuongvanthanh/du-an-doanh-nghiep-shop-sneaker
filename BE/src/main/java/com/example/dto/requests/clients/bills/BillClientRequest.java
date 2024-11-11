package com.example.dto.requests.clients.bills;

import lombok.Builder;
import lombok.Getter;
import com.example.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public abstract class BillClientRequest {

    @Builder
    @Getter
    public static class BillCreate{
        private String bankCode;

        private Long customerId;

        private Long couponId;

        private BigDecimal shipping;

        private BigDecimal subtotal;

        private BigDecimal sellerDiscount;

        private BigDecimal total;

        private PaymentMethod paymentMethod;

        private String message;

        private List<BillDetailCreate> items;
    }

    @Builder
    @Getter
    public static  class BillDetailCreate{
        private Long id;

        private BigDecimal retailPrice;

        private BigDecimal sellPrice;

        private int quantity;
    }
}
