package com.example.dto.response.employees;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PositionResponse {
    private int id;
    private String name;
}
