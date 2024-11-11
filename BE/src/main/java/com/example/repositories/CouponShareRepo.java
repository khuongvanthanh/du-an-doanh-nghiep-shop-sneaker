package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.model.Coupon;
import com.example.model.CouponShare;

import java.util.List;

@Repository
public interface CouponShareRepo extends JpaRepository<CouponShare, Long> {
    List<CouponShare> findByCoupon(Coupon coupon);
}
