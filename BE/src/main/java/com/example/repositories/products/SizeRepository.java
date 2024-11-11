package com.example.repositories.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.model.Size;

import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {

    @Query("FROM Size WHERE isDeleted = false AND name like %:keyword% ORDER BY id DESC")
    List<Size> findSizesByNameAndIsDeletedIsFalse(String keyword);

    boolean existsSizeByName(String name);
}
