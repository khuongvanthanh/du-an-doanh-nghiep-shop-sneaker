package com.example.service;

import com.example.dto.requests.BillStatusDetailRequest;
import com.example.dto.response.bills.BillStatusDetailResponse;
import com.example.model.BillStatusDetail;

import java.util.List;

public interface BillStatusDetailService {
    List<BillStatusDetailResponse> getBillStatusDetailsByBillId(Long billId);
    long addBillStatusDetail(BillStatusDetailRequest request);
}
