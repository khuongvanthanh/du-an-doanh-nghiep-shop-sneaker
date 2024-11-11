package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.example.model.Bill;
import com.example.model.BillDetail;
import com.example.model.ProductDetail;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillDetailRepo extends JpaRepository<BillDetail, Long> {
    Optional<BillDetail> findByBillAndProductDetail(Bill bill, ProductDetail productDetail);
    Optional<BillDetail> findByProductDetailAndBill(ProductDetail productDetail, Bill bill);
    List<BillDetail> findByBill(Bill bill);

    @Modifying
    @Transactional
    @Query("DELETE FROM BillDetail bd WHERE bd.bill.id = :billId")
    void deleteByBillId(@Param("billId") Long billId);
}
