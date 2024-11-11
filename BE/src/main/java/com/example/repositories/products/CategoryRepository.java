package com.example.repositories.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.model.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("FROM Category WHERE isDeleted = false AND name like %:keyword% ORDER BY id DESC")
    List<Category> findCategoriesByNameAndIsDeletedIsFalse(String keyword);

    boolean existsCategoryByName(String name);
}
