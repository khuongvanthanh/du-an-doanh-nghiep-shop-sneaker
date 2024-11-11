package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.Employee;
import com.example.model.Salary;

public interface SalaryRepository extends JpaRepository<Salary,Integer> {
}
