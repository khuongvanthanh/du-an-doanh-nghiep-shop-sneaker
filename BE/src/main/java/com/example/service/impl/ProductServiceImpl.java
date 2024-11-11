package com.example.service.impl;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.dto.requests.common.ProductParamFilter2;
import com.example.dto.requests.productRequests.*;
import com.example.dto.requests.common.ProductParamFilter;
import com.example.dto.response.PageableResponse;
import com.example.dto.response.productResponse.*;
import com.example.enums.ProductStatus;
import com.example.exception.EntityNotFoundException;
import com.example.exception.NotAllowedDeleteEntityException;
import com.example.model.*;
import com.example.repositories.customQuery.ProductCustomizeQuery;
import com.example.repositories.products.*;
import com.example.service.ProductService;
import com.example.utils.CloudinaryUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.utils.UserUtils.getUserById;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final BrandRepository brandRepository;

    private final MaterialRepository materialRepository;

    private final ProductImageRepository productImageRepository;

    private final SizeRepository sizeRepository;

    private final ColorRepository colorRepository;

    private final ProductDetailRepository productDetailRepository;

    private final CloudinaryUtils cloudinary;

    private final ProductCustomizeQuery productCustomizeQuery;

    @Override
    public PageableResponse getAllProducts(ProductParamFilter param) {
        if (param.getPageNo() < 1) {
            param.setPageNo(1);
        }
        return this.productCustomizeQuery.getAllProducts(param);
    }

    @Override
    public PageableResponse getAllProductDetails(ProductParamFilter2 param) {
        if (param.getPageNo() < 1) {
            param.setPageNo(1);
        }
        return this.productCustomizeQuery.getAllProductDetails(param);
    }

    @Override
    public long storeProduct(ProductRequest req) {
        User user = getUserById(req.getUserId());

        // Products
        Product product = new Product();
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setStatus(req.getStatus());

        product.setCategory(this.categoryRepository.findById(req.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("Invalid category")));
        product.setBrand(this.brandRepository.findById(req.getBrandId()).orElseThrow(() -> new EntityNotFoundException("Invalid brand")));
        product.setMaterial(this.materialRepository.findById(req.getMaterialId()).orElseThrow(() -> new EntityNotFoundException("Invalid material")));

        product.setOrigin(req.getOrigin());
        product.setCreatedBy(user);
        product.setUpdatedBy(user);

        product = this.productRepository.save(product);

        // Product details
        for (ProductDetailRequest prd : req.getProductDetails()) {
            ProductDetail productDetail = new ProductDetail();
            productDetail.setProduct(product);
            productDetail.setRetailPrice(prd.getRetailPrice());
            productDetail.setQuantity(prd.getQuantity());
            productDetail.setStatus(req.getStatus());
            productDetail.setSize(this.getSizeById(prd.getSizeId()));
            productDetail.setColor(this.getColorById(prd.getColorId()));

            this.productDetailRepository.save(productDetail);
        }

        return product.getId();
    }

    @Override
    public void storeProductImages(ProductImageReq req) {
        Product product = this.getProductById(req.getProductId());
        for (MultipartFile file : req.getImages()) {
            ProductImage productImage = new ProductImage();
            Map<String, String> uploadResult = this.cloudinary.upload(file);
            productImage.setProduct(product);
            productImage.setImageUrl(uploadResult.get("url"));
            productImage.setPublicId(uploadResult.get("publicId"));
            this.productImageRepository.save(productImage);
        }
    }

    @Override
    public void setProductStatus(long id, ProductStatus status) {
        Product product = this.getProductById(id);
        product.setStatus(status);
        this.productRepository.save(product);
    }

    @Override
    public void moveToBin(Long id) {
        Product product = this.getProductById(id);
        product.setIsDeleted(true);
        this.productRepository.save(product);
    }

    @Override
    public void restore(Long id) {
        Product product = this.getProductById(id);
        product.setIsDeleted(false);
        this.productRepository.save(product);
    }

    @Override
    public void deleteProductForever(Long id) {
        //Todo
        if (id != null) {
            throw new NotAllowedDeleteEntityException("Chưa cho phép xóa vĩnh viễn");
        }
        Product product = getProductById(id);
        product.getProductImages().forEach(item -> {
            ProductImage productImage = getProductImageById(item.getId());
            this.cloudinary.removeByPublicId(productImage.getPublicId());
            this.productImageRepository.delete(productImage);
        });
    }

    @Override
    public ProductModifyRes getProductInfo(long id) {
        return convertToProductResponse(this.getProductById(id));
    }

    @Override
    public void updateProduct(ProductUpdateRequest req, long id) {
        User user = getUserById(req.getUserId());

        Product product = this.getProductById(id);
        product.setName(req.getName());
        product.setDescription(req.getDescription());

        product.setCategory(this.categoryRepository.findById(req.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("Invalid category")));
        product.setBrand(this.brandRepository.findById(req.getBrandId()).orElseThrow(() -> new EntityNotFoundException("Invalid brand")));
        product.setMaterial(this.materialRepository.findById(req.getMaterialId()).orElseThrow(() -> new EntityNotFoundException("Invalid material")));

        product.setOrigin(req.getOrigin());
        product.setUpdatedBy(user);

        this.productRepository.save(product);
    }

    @Override
    public void setProductDetailStatus(long id, boolean status) {
        ProductDetail prd = this.getProductDetailById(id);
        prd.setStatus(status ? ProductStatus.ACTIVE : ProductStatus.INACTIVE);
        this.productDetailRepository.save(prd);
    }

    @Override
    public void updateAttributeProductDetail(List<ProductDetailModify> items) {
        items.forEach(i -> {
            ProductDetail prd = this.getProductDetailById(i.getId());
            prd.setQuantity(i.getQuantity());
            prd.setRetailPrice(i.getPrice());
            this.productDetailRepository.save(prd);
        });
    }

    @Override
    public long storeProductDetailAttribute(ProductDetailStoreRequest request) {
        if (this.productDetailRepository.existsDetailByAttribute(request.getProductId(), request.getColorId(), request.getSizeId())) {
            throw new EntityExistsException("Thuộc tính đã tồn tại!");
        }
        return this.productDetailRepository.save(ProductDetail.builder()
                .color(this.getColorById(request.getColorId()))
                .size(this.getSizeById(request.getSizeId()))
                .retailPrice(request.getRetailPrice())
                .quantity(request.getQuantity())
                .product(this.getProductById(request.getProductId()))
                .status(ProductStatus.ACTIVE)
                .build()).getId();
    }

    @Override
    public void removeImageCloudinary(String publicId) {
        this.cloudinary.removeByPublicId(publicId);
        this.productImageRepository.deleteByPublicId(publicId);
    }

    @Override
    public PageableResponse productArchive(ProductParamFilter param) {
        if (param.getPageNo() < 1) {
            param.setPageNo(1);
        }
        return this.productCustomizeQuery.getAllProductArchives(param);
    }

    private Product getProductById(long id) {
        return this.productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    private ProductImage getProductImageById(long id) {
        return this.productImageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product image not found"));
    }

    private ProductDetail getProductDetailById(long id) {
        return this.productDetailRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product detail not found"));
    }

    private Size getSizeById(int id) {
        return this.sizeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Size not found"));
    }

    private Color getColorById(int id) {
        return this.colorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Color not found"));
    }

    private ProductModifyRes convertToProductResponse(Product product) {
        return ProductModifyRes.builder()
                .name(product.getName())
                .description(product.getDescription())
                .category(CategoryResponse.builder().id(product.getCategory().getId()).name(product.getCategory().getName()).build())
                .brand(BrandResponse.builder().id(product.getBrand().getId()).name(product.getBrand().getName()).build())
                .material(MaterialResponse.builder().id(product.getMaterial().getId()).name(product.getMaterial().getName()).build())
                .origin(product.getOrigin())
                .imageUrl(convertToImageResponse(product.getProductImages()))
                .created_by(product.getCreatedBy().getUsername())
                .modified_by(product.getUpdatedBy().getUsername())
                .created_at(product.getCreateAt())
                .modified_at(product.getUpdateAt())
                .details(convertToProductDetailResponse(product.getProductDetails()))
                .build();
    }

    private List<ProductDetailResponse> convertToProductDetailResponse(List<ProductDetail> productDetails) {
        List<ProductDetailResponse> productDetailResponses = new ArrayList<>();
        productDetails.forEach(productDetail -> {
            productDetailResponses.add(ProductDetailResponse.builder()
                    .id(productDetail.getId())
                    .color(productDetail.getColor().getName())
                    .size(productDetail.getSize().getName())
                    .price(productDetail.getRetailPrice())
                    .quantity(productDetail.getQuantity())
                    .status(productDetail.getStatus())
                    .build());
        });
        return productDetailResponses;
    }

    private List<ImageResponse> convertToImageResponse(List<ProductImage> images) {
        List<ImageResponse> imageResponses = new ArrayList<>();
        images.forEach(imageResponse -> {
            ImageResponse image = ImageResponse.builder()
                    .publicId(imageResponse.getPublicId())
                    .url(imageResponse.getImageUrl())
                    .build();
            imageResponses.add(image);
        });
        return imageResponses;
    }

}
