package com.example.dto.requests.productRequests;

import lombok.Getter;
import lombok.Setter;
import com.example.dto.requests.billRequest.BillDetailRequest;
import com.example.dto.requests.billRequest.BillRequest;

import java.util.List;

@Getter
@Setter
public class BillStoreRequest {
    private BillRequest billRequest;
    private List<BillDetailRequest> billDetails;
}
