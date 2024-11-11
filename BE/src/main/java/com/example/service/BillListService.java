package com.example.service;

import com.example.dto.requests.common.BillEditParamFilter;
import com.example.dto.requests.common.BillListParamFilter;
import com.example.dto.response.PageableResponse;
import com.example.dto.response.bills.BillEditResponse;

import java.util.List;

public interface BillListService {
    PageableResponse getAllBillList(BillListParamFilter param);
    List<BillEditResponse> getAllBillEdit(Long billId);
}
