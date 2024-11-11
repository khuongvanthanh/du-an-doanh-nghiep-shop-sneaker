package com.example.service.clients;

import jakarta.servlet.http.HttpServletRequest;
import com.example.dto.requests.clients.bills.BillClientRequest;
import com.example.dto.requests.clients.cart.CartRequest;
import com.example.dto.requests.productRequests.ProductRequests;
import com.example.dto.response.PageableResponse;
import com.example.dto.response.clients.cart.CartResponse;
import com.example.dto.response.clients.customer.UserInfoRes;
import com.example.dto.response.clients.invoices.InvoiceResponse;
import com.example.dto.response.clients.product.ProductResponse;

import java.util.List;
import java.util.Set;

public interface ClientService {
    List<ProductResponse.Product> getExploreOurProducts(Integer page);

    Set<ProductResponse.Product> getBestSellingProducts();

    ProductResponse.ProductDetail getProductDetail(Long id);

    List<CartResponse.Cart> getCarts(HttpServletRequest request);

    void addToCart(CartRequest.FilterParams req);

    CartResponse.Cart buyNow(CartRequest.FilterParams req);

    void updateCart(CartRequest.Param req);

    void deleteCart(String id, String username);

    UserInfoRes getUserInfo(long id);

    long saveBill(BillClientRequest.BillCreate req);

    PageableResponse getInvoices(InvoiceResponse.Param param);

    void cancelInvoice(long id, String message);

    List<InvoiceResponse.InvoiceStatus> getInvoiceStatuses();

     PageableResponse productFilters(ProductRequests.ParamFilters param);
}
