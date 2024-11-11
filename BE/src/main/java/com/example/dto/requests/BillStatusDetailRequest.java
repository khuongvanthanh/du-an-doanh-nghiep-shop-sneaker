package com.example.dto.requests;

import lombok.Builder;
import lombok.Getter;
import com.example.model.Bill;
import com.example.model.BillStatus;

@Getter
@Builder
public class BillStatusDetailRequest {
    private Long bill;
    private Integer billStatus;
    private String note;
    private Long userId;
}
