package com.example.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import com.example.model.Product;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Builder
public class PromotionResponse {
    private int id;

    private String code;

    private String name;

    private int percent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private Date endDate;

    private String note;

//    private String status;
//
//    private int numberOfProduct;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private Date updatedAt;

    private List<Long> listIdProduct;
}
