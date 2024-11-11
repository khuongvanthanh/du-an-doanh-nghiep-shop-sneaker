package com.example.repositories.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.model.Material;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
    @Query("FROM Material WHERE isDeleted = false AND name like %:keyword% ORDER BY id DESC")
    List<Material> findMaterialsByNameAndIsDeletedIsFalse(String keyword);

    boolean existsMaterialByName(String name);
}
