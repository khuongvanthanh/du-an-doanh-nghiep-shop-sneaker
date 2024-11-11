package com.example.controller;

import com.example.dto.requests.BillStatusDetailRequest;
import com.example.dto.requests.billRequest.BillCustomerRequest;
import com.example.dto.requests.billRequest.BillDetailRequest;
import com.example.dto.requests.billRequest.BillRequest;
import com.example.dto.requests.billRequest.BillStoreRequest;
import com.example.dto.requests.common.BillListParamFilter;
import com.example.dto.response.ResponseData;
import com.example.dto.response.bills.BillCouponResponse;
import com.example.dto.response.bills.BillDetailResponse;
import com.example.dto.response.bills.BillStatusDetailResponse;
import com.example.model.Customer;
import com.example.service.BillListService;
import com.example.service.BillService;
import com.example.service.BillStatusDetailService;
import com.example.service.BillStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/${api.version}/bill")
@Tag(name = "Bill Controller", description = "Quản lý thêm, sửa, xóa hóa đơn")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;
    private final BillListService billListService;
    private final BillStatusService billStatusService;
    private final BillStatusDetailService billStatusDetailService;
    private static final Logger logger = LoggerFactory.getLogger(BillController.class);

    //them lan 1
    @Operation(summary = "Lấy thông tin hóa đơn", description = "Lấy thông tin hóa đơn cụ thể là mã hóa đơn")
    @GetMapping("/getBill")
    public ResponseData<?> getAllBill() {
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy thông tin hóa đơn thành công", this.billService.getAllBill());
    }

    @Operation(summary = "Lấy thông tin hóa đơn chi tiết", description = "Lấy thông tin hóa đơn chi tiết")
    @GetMapping("/getBillDetail/{id}")
    public ResponseData<?> getBillDetail(@PathVariable Long id) {
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy thông tin hóa đơn chi tiết thành công", this.billService.getBillId(id));
    }

    @Operation(summary = "Thêm hóa đơn với chỉ mã", description = "Thêm hóa đơn chỉ với mã mà không cần thông tin khác")
    @PostMapping("/storeBill")
    public ResponseData<?> addBill(@Valid @RequestBody BillRequest billRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Lỗi xác thực", bindingResult.getFieldErrors());
        }
        long billId = billService.storeBill(billRequest);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Thêm mới hóa đơn với mã thành công", billId);
    }

    @Operation(summary = "Xóa hóa đơn", description = "Xóa hóa đơn")
    @DeleteMapping("/deleteBill/{billID}")
    public ResponseData<?> deleteBill(@PathVariable long billID) {
        billService.deleteBill(billID);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Xóa hóa đơn thành công");
    }

    //them lan 2
    @Operation(summary = "Lấy thông tin hóa đơn", description = "Lấy thông tin hóa đơn cụ thể là mã hóa đơn")
    @GetMapping("/getProduct/{billId}")  // Accept billId as a path variable
    public ResponseData<?> getAllProduct(@PathVariable long billId) {
        List<BillDetailResponse> billDetails = this.billService.getAllProducts(billId);
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy thông tin hóa đơn thành công", billDetails);
    }

    @Operation(summary = "Thêm sản phẩm vào hóa đơn", description = "Thêm sản phẩm vào hóa đơn chi tiết ")
    @PostMapping("/storeProduct")
    public ResponseData<?> addProduct(@Valid @RequestBody BillDetailRequest billDetailRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Lỗi xác thực", bindingResult.getFieldErrors());
        }
        long billId = billService.storeProduct(billDetailRequest);
        return new ResponseData<>(HttpStatus.CREATED.value(), "Thêm mới sản phẩm vào hdct thành công", billId);
    }

    @Operation(summary = "Xóa sản phẩm khỏi hóa đơn", description = "Xóa sản phẩm khỏi chi tiết hóa đơn")
    @DeleteMapping("/deleteProduct/{billDetailId}")
    public ResponseData<?> deleteProduct(@PathVariable long billDetailId) {
        billService.deleteBillDetail(billDetailId);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Xóa sản phẩm khỏi hóa đơn thành công");
    }

    //them lan 3
    @Operation(summary = "Lấy tất cả khách hàng", description = "Lấy danh sách tất cả khách hàng")
    @GetMapping("/getCustomer/{billId}")
    public ResponseData<?> getAllCustomers(@PathVariable long billId) {
        List<Customer> customers = billService.getAllCustomers(billId);
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy danh sách khách hàng thành công", customers);
    }

    @Operation(summary = "Thêm khách hàng vào hóa đơn", description = "Thêm khách hàng vào hóa đơn hiện có")
    @PostMapping("/storeCustomer")
    public ResponseData<?> addCustomer(@RequestParam Long billId, @RequestParam Long customerId) {
        long updatedBillId = billService.storeCustomer(billId, customerId);
        return new ResponseData<>(HttpStatus.OK.value(), "Thêm khách hàng vào hóa đơn thành công", updatedBillId);
    }

    @Operation(
            summary = "Update Customer",
            description = "Update customer information in the database"
    )
    @PutMapping("/update/{id}")
    public ResponseData<?> updateCustomer(@PathVariable Long id, @Valid @RequestBody BillCustomerRequest billCustomerRequest) {
        return new ResponseData<>(HttpStatus.OK.value(), "Sửa thông tin khách hàng thành công", billService.updateCustomer(id, billCustomerRequest));
    }

    @Operation(summary = "Xóa khách hàng khỏi hóa đơn", description = "Xóa khách hàng khỏi hóa đơn hiện có")
    @PostMapping("/deleteCustomer")
    public ResponseData<?> deleteCustomer(@RequestParam Long billId) {
        long updatedBillId = billService.deleteCustomer(billId); // Call the service method to update the bill
        return new ResponseData<>(HttpStatus.OK.value(), "Khách hàng đã được xóa khỏi hóa đơn thành công", updatedBillId);
    }

    //them lan 4
    @Operation(summary = "Lấy tất cả mã giảm giá", description = "Lấy danh sách tất cả mã giảm giá")
    @GetMapping("/getCoupon/{billId}")
    public ResponseData<?> getAllCoupons(@PathVariable Long billId) {
        List<BillCouponResponse> coupons = billService.getAllCoupons(billId);
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy danh sách mã giảm giá thành công", coupons);
    }

    @Operation(summary = "Thêm mã giảm giá vào hóa đơn", description = "Thêm mã giảm giá vào hóa đơn hiện có")
    @PostMapping("/storeCoupon")
    public ResponseData<?> addCoupon(@RequestParam Long billId, @RequestParam Long couponId) {
        long updatedBillId = billService.storeCoupon(billId, couponId);
        return new ResponseData<>(HttpStatus.OK.value(), "Thêm mã giảm giá vào hóa đơn thành công", updatedBillId);
    }

    @Operation(summary = "Xóa phiếu giảm giá khỏi hóa đơn", description = "Xóa hiếu giảm giá khỏi hóa đơn hiện có")
    @PostMapping("/deleteCoupon")
    public ResponseData<?> deleteCoupon(@RequestParam Long billId) {
        long updatedBillId = billService.deleteCoupon(billId); // Call the service method to update the bill
        return new ResponseData<>(HttpStatus.OK.value(), "Phiếu giảm giá đã được xóa khỏi hóa đơn thành công", updatedBillId);
    }

    //them cuoi cung
    @Operation(summary = "Thêm mới hóa đơn", description = "Thêm hóa đơn và các chi tiết hóa đơn vào cơ sở dữ liệu")
    @PostMapping("/storePay")
    public ResponseData<?> addPay(@Valid @RequestBody BillStoreRequest billStoreRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Lỗi xác thực", bindingResult.getFieldErrors());
        }
        if (billStoreRequest.getBillRequest() == null) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "BillRequest cannot be null", null);
        }
        BillRequest billRequest = billStoreRequest.getBillRequest();
        List<BillDetailRequest> billDetails = billStoreRequest.getBillDetails();
        if (billDetails == null || billDetails.isEmpty()) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "BillDetails cannot be empty", null);
        }
        logger.info("Received request to add pay: {}", billStoreRequest);
//        try {
            long billId = billService.storePay(billRequest, billDetails);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Thêm mới hóa đơn thành công", billId);
//        } catch (IllegalArgumentException e) {
////            logger.error("Error adding pay: {}", e.getMessage());
//            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
//        } catch (Exception e) {
//            logger.error("Unexpected error occurred while adding pay: ", e);
//            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Đã xảy ra lỗi khi thêm hóa đơn", null);
//        }
    }

    //---------------------------------------------------------------------BILL LIST-------------------------------------------------------------------------//
    @Operation(summary = "Dách sách bảng bill chi tiết", description = "Dách sách bảng bill chi tiết")
    @GetMapping("/billList")
    public ResponseData<?> getAllBillList(BillListParamFilter param) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get order Successfully", this.billListService.getAllBillList(param));
    }

    @Operation(summary = "Dách sách bill chi tiết theo id hóa đơn", description = "Dách sách bill chi tiết theo id hóa đơn")
    @GetMapping("/billEdit/{billId}")
    public ResponseData<?> getAllBillEdit(@PathVariable Long billId) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get order Successfully", this.billListService.getAllBillEdit(billId));
    }

    @Operation(summary = "Dách sách status", description = "Dách sách status")
    @GetMapping("/status")
    public ResponseData<?> getAllStatus() {
        return new ResponseData<>(HttpStatus.OK.value(), "Get status Successfully", this.billStatusService.getAllBillStatus());
    }

    @Operation(summary = "Thêm mới chi tiết trạng thái hóa đơn", description = "Thêm mới chi tiết trạng thái hóa đơn")
    @PostMapping("/addBillStatusDetail")
    public ResponseData<?> addBillStatusDetail(@RequestBody BillStatusDetailRequest request) {
        long id = billStatusDetailService.addBillStatusDetail(request);
        return new ResponseData<>(HttpStatus.OK.value(), "Bill status detail added successfully", id);
    }

    @Operation(summary = "Danh sách chi tiết trạng thái hóa đơn theo id hóa đơn", description = "Lấy danh sách chi tiết trạng thái hóa đơn theo id hóa đơn")
    @GetMapping("/billStatusDetails/{billId}")
    public ResponseData<?> getBillStatusDetailsByBillId(@PathVariable Long billId) {
        List<BillStatusDetailResponse> billStatusDetails = billStatusDetailService.getBillStatusDetailsByBillId(billId);
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy danh sách chi tiết trạng thái hóa đơn thành công", billStatusDetails);
    }

}
