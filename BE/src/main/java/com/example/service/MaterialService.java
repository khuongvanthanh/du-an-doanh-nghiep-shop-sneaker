package com.example.service;

import com.example.dto.requests.productRequests.MaterialRequest;
import com.example.dto.response.productResponse.MaterialResponse;

import java.util.List;

public interface MaterialService {
    List<MaterialResponse> getAllMaterials(String keyword);

    Integer storeMaterial(MaterialRequest req);

    void updateMaterial(MaterialRequest req, Integer id);

    void isDeleteBrand(Integer id);
}
