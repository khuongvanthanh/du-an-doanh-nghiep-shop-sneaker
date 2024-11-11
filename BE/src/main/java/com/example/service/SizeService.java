package com.example.service;

import com.example.dto.requests.productRequests.SizeRequest;
import com.example.dto.response.productResponse.SizeResponse;

import java.util.List;

public interface SizeService {
    List<SizeResponse> getAllSizes(String keyword);

    int storeSize(SizeRequest req);

    void updateSize(SizeRequest req, int id);

    void isDeleteSize(int id);
}
