package com.example.repositories.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.model.Brand;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    @Query("FROM Brand WHERE isDeleted = false AND name like %:keyword% ORDER BY id DESC")
    List<Brand> findBrandsByNameAndIsDeletedIsFalse(String keyword);

    boolean existsBrandByName(String name);
}
