package com.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.dto.requests.BillStatusDetailRequest;
import com.example.dto.response.bills.BillStatusDetailResponse;
import com.example.exception.EntityNotFoundException;
import com.example.model.Bill;
import com.example.model.BillStatus;
import com.example.model.BillStatusDetail;
import com.example.model.User;
import com.example.repositories.BillRepo;
import com.example.repositories.BillStatusDetailRepo;
import com.example.repositories.BillStatusRepo;
import com.example.repositories.auth.UserRepository;
import com.example.repositories.customQuery.BillCustomizeQuery;
import com.example.service.BillStatusDetailService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillStatusDetailServiceImpl implements BillStatusDetailService {

    private final BillRepo billRepository;
    private final BillStatusRepo billStatusRepository;
    private final BillStatusDetailRepo billStatusDetailRepo;
    private final UserRepository userRepository;
    private final BillCustomizeQuery billCustomizeQuery;

    @Override
    public List<BillStatusDetailResponse> getBillStatusDetailsByBillId(Long billId) {
        return this.billCustomizeQuery.getBillStatusDetailsByBillId(billId);
    }

    @Override
    public long addBillStatusDetail(BillStatusDetailRequest request) {
        Bill bill = billRepository.findById(request.getBill())
                .orElseThrow(() -> new IllegalArgumentException("Bill not found"));
        BillStatus billStatus = billStatusRepository.findById(request.getBillStatus())
                .orElseThrow(() -> new IllegalArgumentException("BillStatus not found"));

        BillStatusDetail billStatusDetail = new BillStatusDetail();
        billStatusDetail.setBill(bill);
        billStatusDetail.setBillStatus(billStatus);
        billStatusDetail.setNote(request.getNote());

        billStatusDetail.setCreatedBy(getUserById(request.getUserId()));
        billStatusDetail.setUpdatedBy(getUserById(request.getUserId()));
        this.billStatusDetailRepo.save(billStatusDetail);
        return billStatusDetail.getId();
    }

    private User getUserById(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy user"));
    }
}
