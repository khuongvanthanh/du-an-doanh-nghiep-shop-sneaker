package com.example.dto.requests.productRequests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import com.example.enums.ProductStatus;

import java.util.Set;

@Getter
@Builder
public class ProductRequest {
    @NotBlank(message = "Vui lòng nhập tên sản phẩm")
    private String name;

    private String description;

    private ProductStatus status;

    @NotNull(message = "Vui lòng nhập danh mục sản phẩm")
    private Integer categoryId;

    @NotNull(message = "Vui lòng nhập thương hiệu sản phẩm")
    private Integer brandId;

    @NotNull(message = "Vui lòng nhập chất liệu sản phẩm")
    private Integer materialId;

    @NotBlank(message = "Vui lòng nhập nơi xuất xứ")
    private String origin;

    @NotNull(message = "Chưa có thuộc tính sản phẩm!")
    private Set<ProductDetailRequest> productDetails;

    @NotNull(message = "User is null!")
    private Long userId;
}
