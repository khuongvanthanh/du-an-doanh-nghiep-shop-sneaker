package com.example.service;

import com.example.dto.requests.productRequests.ColorRequest;
import com.example.dto.response.productResponse.ColorResponse;

import java.util.List;

public interface ColorService {
    List<ColorResponse> getAllColors(String keyword);

    int storeColor(ColorRequest request);

    void updateColor(ColorRequest request, int id);

    void isDeleteColor(int id);
}
