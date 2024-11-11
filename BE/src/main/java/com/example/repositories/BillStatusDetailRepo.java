package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.model.BillStatusDetail;

@Repository
public interface BillStatusDetailRepo extends JpaRepository<BillStatusDetail, Long> {
}
