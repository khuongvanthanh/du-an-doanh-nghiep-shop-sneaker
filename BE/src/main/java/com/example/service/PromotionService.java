package com.example.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.dto.requests.PromotionDetailRequest;
import com.example.dto.requests.PromotionRequest;
import com.example.dto.response.EmployeeResponse;
import com.example.dto.response.PromotionDetailResponse;
import com.example.dto.response.PromotionResponse;
import com.example.model.PromotionDetail;

import java.util.Date;
import java.util.List;

public interface PromotionService {
    List<PromotionResponse> getAllPromotion();

    List<PromotionDetailResponse> getAllPromotionDetail();

    PromotionResponse getPromotionId(Integer id);

    Integer storePromotion(PromotionRequest promotionRequest);

//    Integer storePromotionDetail(PromotionDetailRequest promotionDetailRequest);

//    PromotionResponse getPromotionInfo(Integer id);

    Integer updatePromotion(PromotionRequest req, Integer id);

    void deleteByPromotionId(Integer id);

    Page<PromotionResponse> getPromotion(Pageable pageable);

   Page<PromotionResponse> searchPromotions(Date startDate, Date endDate, String name, Pageable pageable);

    Page<PromotionResponse> findByKeywordAndDate(String keyword, Date startDate, Date endDate, String status, Pageable pageable);
//
//    Page<PromotionResponse> findByKeywordAndDate(String keyword, Date startDate, Date endDate, String status, Pageable pageable);
}
