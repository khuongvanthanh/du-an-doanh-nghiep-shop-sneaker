package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.CustomerAddress;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {
}
