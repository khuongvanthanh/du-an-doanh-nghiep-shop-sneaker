package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.EmployeeAddress;

public interface Employee_addressRepository extends JpaRepository<EmployeeAddress,Integer> {
}
