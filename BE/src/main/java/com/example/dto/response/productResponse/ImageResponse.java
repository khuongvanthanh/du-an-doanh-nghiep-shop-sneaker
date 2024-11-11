package com.example.dto.response.productResponse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageResponse {
    private String publicId;
    private String url;
}
