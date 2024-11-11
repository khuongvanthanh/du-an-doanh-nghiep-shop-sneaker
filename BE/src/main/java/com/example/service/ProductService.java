package com.example.service;

import com.example.dto.requests.common.ProductParamFilter2;
import com.example.dto.requests.productRequests.*;
import com.example.dto.requests.common.ProductParamFilter;
import com.example.dto.response.PageableResponse;
import com.example.dto.response.productResponse.ProductModifyRes;
import com.example.enums.ProductStatus;

import java.util.List;

public interface ProductService {

    PageableResponse getAllProducts(ProductParamFilter param);

    PageableResponse getAllProductDetails(ProductParamFilter2 param);

    long storeProduct(ProductRequest req);

    void storeProductImages(ProductImageReq req);

    void setProductStatus(long id, ProductStatus status);

    void moveToBin(Long id);

    void restore(Long id);

    void deleteProductForever(Long id);

    ProductModifyRes getProductInfo(long id);

    void updateProduct(ProductUpdateRequest req, long id);

    void setProductDetailStatus(long id, boolean status);

    void updateAttributeProductDetail(List<ProductDetailModify> items);

    long storeProductDetailAttribute(ProductDetailStoreRequest request);

    void removeImageCloudinary(String publicId);

    PageableResponse productArchive(ProductParamFilter param);
}
