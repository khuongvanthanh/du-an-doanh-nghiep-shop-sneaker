package com.example.service.impl;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.dto.requests.productRequests.BrandRequest;
import com.example.dto.response.productResponse.BrandResponse;
import com.example.exception.EntityNotFoundException;
import com.example.exception.NotAllowedDeleteEntityException;
import com.example.model.Brand;
import com.example.model.User;
import com.example.repositories.products.BrandRepository;
import com.example.repositories.products.ProductRepository;
import com.example.repositories.auth.UserRepository;
import com.example.service.BrandService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    @Override
    public List<BrandResponse> getAllBrands(String keyword) {
        return this.brandRepository.findBrandsByNameAndIsDeletedIsFalse(keyword).stream().map(this::convertToBrandResponse).toList();
    }

    @Transactional
    @Override
    public Integer storeBrand(BrandRequest req) {
        if (this.brandRepository.existsBrandByName(req.getName().trim())) {
            throw new EntityExistsException("Thương hiệu " + req.getName() + " đã tồn tại");
        }
        User user = userRepository.findById(req.getUserId()).orElse(null);
        Brand brand = new Brand();
        brand.setName(req.getName());
        brand.setCreatedBy(user);
        brand.setUpdatedBy(user);
        return this.brandRepository.save(brand).getId();
    }

    @Transactional
    @Override
    public void updateBrand(BrandRequest req, Integer id) {
        Brand brand = this.getBrandById(id);
        if (!Objects.equals(brand.getName(), req.getName().trim())) {
            if (this.brandRepository.existsBrandByName(req.getName().trim())) {
                throw new EntityExistsException("Thương hiệu " + req.getName() + " đã tồn tại");
            }
        }
        brand.setName(req.getName());
        brand.setUpdatedBy(this.getUserById(req.getUserId()));
        this.brandRepository.save(brand);
    }

    @Override
    public void isDeleteBrand(Integer id) {
        Brand brand = this.getBrandById(id);
        try {
            this.brandRepository.delete(brand);
        } catch (Exception e) {
            throw new NotAllowedDeleteEntityException("Không thể xóa thương hiệu này!");
        }
    }

    private Brand getBrandById(Integer id) {
        return this.brandRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Brand not found"));
    }

    private User getUserById(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private BrandResponse convertToBrandResponse(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .productCount(this.productRepository.countByBrand(brand.getId()))
                .createdBy("Admin")
                .createdAt(brand.getCreateAt())
                .updatedAt(brand.getUpdateAt())
                .build();
    }

}
