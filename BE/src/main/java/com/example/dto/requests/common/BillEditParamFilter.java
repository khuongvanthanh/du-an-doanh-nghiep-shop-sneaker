package com.example.dto.requests.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BillEditParamFilter {
    private Long billId;
    private Integer status;
}
