package com.example.service;

import com.example.dto.requests.productRequests.BrandRequest;
import com.example.dto.response.productResponse.BrandResponse;

import java.util.List;

public interface BrandService {
    List<BrandResponse> getAllBrands(String keyword);

    Integer storeBrand(BrandRequest req);

    void updateBrand(BrandRequest req, Integer id);

    void isDeleteBrand(Integer id);
}
