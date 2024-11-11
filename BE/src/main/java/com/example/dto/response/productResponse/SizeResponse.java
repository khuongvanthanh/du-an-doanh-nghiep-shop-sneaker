package com.example.dto.response.productResponse;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class SizeResponse {
    private Integer id;
    private String name;
    private float length;
    private float width;
    private float sleeve;
    private String createdBy;
    private Date createdAt;
    private Date updatedAt;

    public SizeResponse(Integer id, String name, float length, float width, float sleeve, String createdBy, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.width = width;
        this.sleeve = sleeve;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
