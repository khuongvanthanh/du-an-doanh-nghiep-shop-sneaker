package com.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.model.BillStatus;
import com.example.repositories.BillStatusRepo;
import com.example.service.BillStatusService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillStatusServiceImpl implements BillStatusService {

    private final BillStatusRepo billStatusRepo;

    @Override
    public List<BillStatus> getAllBillStatus() {
        return this.billStatusRepo.findAll();
    }
}
