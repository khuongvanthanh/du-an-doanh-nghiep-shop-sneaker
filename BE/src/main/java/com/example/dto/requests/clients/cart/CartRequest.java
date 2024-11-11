package com.example.dto.requests.clients.cart;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
public abstract class CartRequest {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Param {

        private Long productDetailId;

        private Integer quantity;

        private String username;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FilterParams {
        private Long productId;

        private int sizeId;

        private int colorId;

        private int quantity;

        private String username;
    }
}
