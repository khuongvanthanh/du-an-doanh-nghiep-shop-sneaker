package com.example.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.dto.requests.EmployeeReq;
import com.example.dto.requests.employees.EmployeeImageReq;
import com.example.dto.response.EmployeeResponse;
import com.example.model.Employee;



import java.util.List;

public interface EmployeeService {

    EmployeeResponse getEmployeeById(Long id);

    void deleteEmployee(Long id);

    Page<EmployeeResponse> getEmployee(Pageable pageable);

    int storeEmployee(EmployeeReq req);

    void setUserLocked(long id,Boolean isLocked);

    void updateEmp(EmployeeReq req, Long id);

    List<EmployeeResponse> findByNameAndPhone(String keyword, String phone_number);

    void updateImage(EmployeeImageReq req);
}
