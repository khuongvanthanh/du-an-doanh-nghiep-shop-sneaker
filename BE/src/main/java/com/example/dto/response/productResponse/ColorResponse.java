package com.example.dto.response.productResponse;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class ColorResponse {
    private Integer id;
    private String name;
    private String hex_code;
    private String createdBy;
    private Date createdAt;
    private Date updatedAt;

    public ColorResponse(Integer id, String name, String hex_code, String createdBy, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.hex_code = hex_code;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
