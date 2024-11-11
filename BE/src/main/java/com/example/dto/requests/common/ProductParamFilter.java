package com.example.dto.requests.common;

import lombok.Getter;
import lombok.Setter;
import com.example.enums.ProductStatus;

@Getter
@Setter
public class ProductParamFilter {
    private Integer pageNo = 1;
    private Integer pageSize = 3;
    private String keyword;
    private ProductStatus status = ProductStatus.ALL;
    private String category;
    private String brand;
    private String material;
    private String origin;
}
