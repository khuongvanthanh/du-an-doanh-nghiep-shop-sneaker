package com.example.dto.requests.employees;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressRequest {
    @NotBlank
    private String streetName;

    @NotBlank
    private String ward;

    @NotBlank
    private String district;

    @NotNull
    private Integer districtId;

    @NotBlank
    private String province;

    @NotNull
    private Integer provinceId;
}
