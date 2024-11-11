package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.model.BillStatus;

@Repository
public interface BillStatusRepo extends JpaRepository<BillStatus, Integer> {
}
