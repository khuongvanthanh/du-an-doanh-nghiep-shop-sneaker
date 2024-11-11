package com.example.dto.response.productResponse;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class MaterialResponse {
    private Integer id;
    private String name;
    private Long productCount;
    private String createdBy;
    private Date createdAt;
    private Date updatedAt;
}
