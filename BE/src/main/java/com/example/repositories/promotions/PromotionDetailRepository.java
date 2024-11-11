package com.example.repositories.promotions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.example.model.PromotionDetail;

@Repository
public interface PromotionDetailRepository extends JpaRepository<PromotionDetail, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM PromotionDetail pd WHERE pd.promotion.id = :promotionId")
    void deleteByPromotionId(@Param("promotionId") Integer promotionId);

    @Query("FROM PromotionDetail  WHERE product.id = :productId AND CURRENT_TIMESTAMP BETWEEN promotion.startDate AND promotion.endDate")
    PromotionDetail findByProductId(Long productId);
}
