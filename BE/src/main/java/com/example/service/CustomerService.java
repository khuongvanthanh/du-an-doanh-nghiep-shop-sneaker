package com.example.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.dto.requests.CustomerReq;
import com.example.dto.requests.productRequests.CustomerRequest;
import com.example.dto.requests.productRequests.ProductImageReq;
import com.example.dto.response.CustomerResponse;
import com.example.dto.response.auth.UserResponse;
import com.example.enums.Gender;
import com.example.enums.ProductStatus;
import com.example.model.Customer;
import com.example.model.User;
import com.example.repositories.auth.UserRepository;

import java.util.Date;
import java.util.List;

public interface CustomerService {

    Page<CustomerResponse> getAll(Pageable pageable);  // Modified method to add pagination

    CustomerResponse getCustomerById(Long id);

    long createCustomer(CustomerReq customerReq);

    long updateCustomer(Long id, CustomerRequest customerRequest);

    void deleteCustomer(Long id);

    void setUserLocked(long id,Boolean isLocked);

    Page<CustomerResponse> searchCustomers(String keyword, Gender gender,Date birth, Pageable pageable);

    void updateImage(ProductImageReq req);
}
