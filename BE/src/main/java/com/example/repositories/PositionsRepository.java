package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.Employee;
import com.example.model.Positions;

public interface PositionsRepository extends JpaRepository<Positions,Integer> {
}
