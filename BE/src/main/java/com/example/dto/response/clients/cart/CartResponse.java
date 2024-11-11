package com.example.dto.response.clients.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

public abstract class CartResponse {

    @Builder
    @Getter
    public static class Cart {

        private String id;

        private String imageUrl;

        private String name;

        private String origin;

        private BigDecimal retailPrice;

        private int quantity;

        private ProductCart productCart;

        private String username;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    public static class ProductCart {
        private long id;

        private boolean status;

        private int quantity;

        private BigDecimal sellPrice;

        private Integer percent;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String message;
    }
}
