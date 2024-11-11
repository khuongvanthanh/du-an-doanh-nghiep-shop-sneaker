package com.example.service;

import com.example.dto.requests.productRequests.CategoryRequest;
import com.example.dto.response.productResponse.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories(String keyword);

    Integer storeCategory(CategoryRequest req);

    void updateCategory(CategoryRequest req, Integer id);

    void isDeleteCategory(Integer id);
}
