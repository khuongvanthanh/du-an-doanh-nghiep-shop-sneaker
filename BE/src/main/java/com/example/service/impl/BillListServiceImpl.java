package com.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.dto.requests.common.BillEditParamFilter;
import com.example.dto.requests.common.BillListParamFilter;
import com.example.dto.response.PageableResponse;
import com.example.dto.response.bills.BillEditResponse;
import com.example.dto.response.clients.invoices.InvoiceResponse;
import com.example.repositories.customQuery.BillCustomizeQuery;
import com.example.service.BillListService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillListServiceImpl implements BillListService {

    private final BillCustomizeQuery billCustomizeQuery;

    @Override
    public PageableResponse getAllBillList(BillListParamFilter param) {
        if (param.getPageNo() < 1) {
            param.setPageNo(1);
        }
        return this.billCustomizeQuery.getAllBillList(param);
    }

    @Override
    public List<BillEditResponse> getAllBillEdit(Long billId) {
        return this.billCustomizeQuery.getAllBillEdit(billId);
    }
}
