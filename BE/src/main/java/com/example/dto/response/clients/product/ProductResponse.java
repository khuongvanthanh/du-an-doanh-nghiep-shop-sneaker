package com.example.dto.response.clients.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import com.example.dto.response.productResponse.ColorResponse;
import com.example.dto.response.productResponse.SizeResponse;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public abstract class ProductResponse {

    @Getter
    @Builder
    public static class Product {

        private Long productId;

        private String imageUrl;

        private String name;

        private BigDecimal retailPrice;

        private BigDecimal discountPrice;

        private float rate;

        private long rateCount;

        private Integer percent;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Date expiredDate;

        public Product(Long productId, String imageUrl, String name, BigDecimal retailPrice, BigDecimal discountPrice, float rate, long rateCount, Integer percent, Date expiredDate) {
            this.productId = productId;
            this.imageUrl = imageUrl;
            this.name = name;
            this.retailPrice = retailPrice;
            this.discountPrice = discountPrice;
            this.rate = rate;
            this.rateCount = rateCount;
            this.percent = percent;
            this.expiredDate = expiredDate;
        }
    }

    @Getter
    @Builder
    public static class ProductDetail {
        private Long productId;

        private List<String> imageUrl;

        private String name;

        private BigDecimal retailPrice;

        private BigDecimal discountPrice;

        private Integer percent;

        private float rate;

        private long rateCount;

        private HashSet<SizeResponse> sizes;

        private HashSet<ColorResponse> colors;

        private long quantity;

        private String origin;

        private String category;

        private String material;

        private String brand;

        private String description;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Date expiredDate;

        private List<ProductResponse.Product> relatedItem;
    }
}
