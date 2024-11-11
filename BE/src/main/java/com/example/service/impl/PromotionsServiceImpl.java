package com.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.dto.requests.PromotionRequest;
import com.example.dto.response.EmployeeResponse;
import com.example.dto.response.PromotionDetailResponse;
import com.example.dto.response.PromotionResponse;
import com.example.exception.EntityNotFoundException;
import com.example.model.*;
import com.example.repositories.auth.UserRepository;
import com.example.repositories.products.ProductRepository;
import com.example.repositories.promotions.PromotionDetailRepository;
import com.example.repositories.promotions.PromotionRepository;
import com.example.service.PromotionService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionsServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    private final PromotionDetailRepository promotionDetailRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    @Override
    public List<PromotionResponse> getAllPromotion() {
        return this.promotionRepository.findAll().stream().map(item ->
                PromotionResponse.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .code(item.getCode())
                        .percent(item.getPercent())
                        .startDate(item.getStartDate())
                        .endDate(item.getEndDate())
                        .note(item.getNote())
                        .build()
        ).toList();
    }

    @Override
    public PromotionResponse getPromotionId(Integer id) {
        Promotion promotion = this.promotionRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Promotion not found"));
        return convertPromotionResponsse(promotion);
    }

    @Override
    public List<PromotionDetailResponse> getAllPromotionDetail(){
        return this.promotionDetailRepository.findAll().stream().map(item ->
                PromotionDetailResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .promotionId(item.getPromotion().getId())
                        .build()
        ).toList();
    }

    @Override
    public Integer storePromotion(PromotionRequest req) {
        Promotion promotion = this.promotionRepository.save(Promotion.builder()
                        .name(req.getName())
                        .percent(req.getPercent())
                        .code(req.getCode())
                        .startDate(req.getStartDate())
                        .endDate(req.getEndDate())
                        .note(req.getNote())
                .build());

        for (Long productId : req.getProductIds()) {
            com.example.model.PromotionDetail promotionDetail = com.example.model.PromotionDetail.builder()
                    .promotion(promotion)
                    .product(getProduct(productId))
                    .build();
            this.promotionDetailRepository.save(promotionDetail);
        }
        return promotion.getId();
    }


    @Override
    public Integer updatePromotion(PromotionRequest req, Integer id) {
        Promotion promotion = this.promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with id " + id));

        // Cập nhật thông tin khuyến mãi
        promotion.setName(req.getName());
        promotion.setPercent(req.getPercent());
        promotion.setCode(req.getCode());
        promotion.setStartDate(req.getStartDate());
        promotion.setEndDate(req.getEndDate());
        promotion.setNote(req.getNote());
        populatePromotionData(promotion, req);

        // Lưu lại thông tin đã cập nhật
        this.promotionRepository.save(promotion);

        List<Long> existingProductIds = promotion.getPromotionDetails().stream()
                .map(detail -> detail.getProduct().getId())
                .collect(Collectors.toList());

        // Thêm mới các chi tiết khuyến mãi dựa trên danh sách productIds từ request
        for (PromotionDetail detail : new ArrayList<>(promotion.getPromotionDetails())) {
            if (!req.getProductIds().contains(detail.getProduct().getId())) {
                this.promotionDetailRepository.delete(detail);
                promotion.getPromotionDetails().remove(detail);
            }
        }

        // Thêm mới các PromotionDetail cho các productIds chưa có trong existingProductIds
        for (Long productId : req.getProductIds()) {
            if (!existingProductIds.contains(productId)) {
                PromotionDetail promotionDetail = PromotionDetail.builder()
                        .promotion(promotion)
                        .product(getProduct(productId))
                        .build();
                this.promotionDetailRepository.save(promotionDetail);
                promotion.getPromotionDetails().add(promotionDetail);
            }
        }
        return promotion.getId();
    }

    @Override
    public void deleteByPromotionId(Integer id) {
        Promotion promotion = this.promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found with id " + id));

        promotionDetailRepository.deleteByPromotionId(id);

        promotionRepository.delete(promotion);

    }

    private void populatePromotionData(Promotion promotion, PromotionRequest promotionRequest) {//lay du lieu phieu giam gia request de them
        promotion.setName(promotionRequest.getName());
        promotion.setCode(promotionRequest.getCode());
        promotion.setPercent(promotionRequest.getPercent());
        promotion.setStartDate(promotionRequest.getStartDate());
        promotion.setEndDate(promotionRequest.getEndDate());
        promotion.setNote(promotionRequest.getNote());
//        promotion.setPromotionDetails(promotionRequest.getProductIds());
    }

    private PromotionResponse convertPromotionResponsse(Promotion promotion){
        List<Product> products = promotion.getPromotionDetails().stream()
                .map(PromotionDetail::getProduct)
                .collect(Collectors.toList());
        List<Long> list = new ArrayList<>();

        products.forEach(i -> {
            list.add(i.getId());
        });
        return PromotionResponse.builder()
                .id(promotion.getId())
                .name(promotion.getName())
                .code(promotion.getCode())
                .percent(promotion.getPercent())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .note(promotion.getNote())
                .listIdProduct(list)
                .build();
    }

    @Transactional
    @Override
    public Page<PromotionResponse> searchPromotions(Date startDate, Date endDate, String name, Pageable pageable) {
        Page<Promotion> promotions = promotionRepository.searchPromotions(startDate, endDate, name, pageable);
        return promotions.map(this::convertPromotionResponsse);  // Convert entity to response DTO
    }

    @Override
    public Page<PromotionResponse> findByKeywordAndDate(String keyword, Date startDate, Date endDate,
                                                     String status, Pageable pageable) {
        Page<Promotion> promotions;

        // Kiểm tra nếu không có điều kiện tìm kiếm, trả về toàn bộ danh sách
        if ((keyword == null || keyword.isEmpty()) && startDate == null && endDate == null &&
                 (status == null || status.isEmpty())) {
            promotions = promotionRepository.findAll(pageable);  // Lấy toàn bộ danh sách với phân trang
        } else {
            // Nếu có điều kiện tìm kiếm, gọi hàm findByKeywordAndDate
            promotions = promotionRepository.findByKeywordAndDate(keyword, startDate, endDate, status, pageable);
        }

        // Chuyển đổi từ entity Coupon sang DTO CouponResponse
        return promotions.map(this::convertPromotionResponsse);
    }

    @Override
    public Page<PromotionResponse> getPromotion(Pageable pageable) {
        return promotionRepository.findAll(pageable).map(this::convertPromotionResponsse);
    }

    private Product getProduct(Long id) {
        return this.productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found!"));
    }

    private User getUser(Long id){
        return this.userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found!"));
    }
}
