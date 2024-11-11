package com.example.service;

import com.example.dto.requests.billRequest.BillCustomerRequest;
import com.example.dto.requests.billRequest.BillDetailRequest;
import com.example.dto.requests.billRequest.BillRequest;
import com.example.dto.requests.productRequests.CustomerRequest;
import com.example.dto.response.bills.BillCouponResponse;
import com.example.dto.response.bills.BillDetailResponse;
import com.example.dto.response.bills.BillResponse;
import com.example.model.Customer;

import java.util.List;

public interface BillService {
    //them lan 1
    List<BillResponse> getAllBill();
    List<BillResponse> getAllBills();
    BillResponse getBillId(Long id);
    long storeBill(BillRequest billRequest);
    void deleteBill(long id);
    //them lan 2
    List<BillDetailResponse> getAllProducts(long billId);
    long storeProduct(BillDetailRequest billDetailRequest);
    void deleteBillDetail(long billDetailId);
    //them lan 3
    List<Customer> getAllCustomers(long billId);
    long storeCustomer(Long billId, Long customerId);
    long updateCustomer(Long id, BillCustomerRequest billCustomerRequest);
    long deleteCustomer(long billId);
    //them lan 4
    List<BillCouponResponse> getAllCoupons(long billId);
    long storeCoupon(Long billId, Long couponId);
    long deleteCoupon(long billId);
    //them lan cuoi
    long storePay(BillRequest billRequest, List<BillDetailRequest> billDetailRequests);
    //thanh toan
}
