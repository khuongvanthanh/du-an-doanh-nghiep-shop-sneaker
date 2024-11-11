package com.example.service.clients.impl;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.dto.requests.clients.bills.BillClientRequest;
import com.example.dto.requests.clients.cart.CartRequest;
import com.example.dto.requests.productRequests.ProductRequests;
import com.example.dto.response.PageableResponse;
import com.example.dto.response.clients.cart.CartResponse;
import com.example.dto.response.clients.customer.UserInfoRes;
import com.example.dto.response.clients.invoices.InvoiceResponse;
import com.example.dto.response.clients.product.ProductResponse;
import com.example.enums.PaymentMethod;
import com.example.enums.ProductStatus;
import com.example.exception.EntityNotFoundException;
import com.example.exception.InvalidDataException;
import com.example.model.*;
import com.example.model.redis_model.Cart;
import com.example.repositories.*;
import com.example.repositories.customQuery.ProductCustomizeQuery;
import com.example.repositories.invoice_client.InvoiceRepository;
import com.example.repositories.products.ProductDetailRepository;
import com.example.repositories.products.ProductRepository;
import com.example.repositories.promotions.PromotionDetailRepository;
import com.example.service.JwtService;
import com.example.service.clients.ClientService;
import com.example.utils.RandomNumberGenerator;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static com.example.enums.TokenType.ACCESS_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ProductCustomizeQuery productCustomizeQuery;

    private final ProductRepository productRepository;

    private final ProductDetailRepository productDetailRepository;

    private final CartRepository cartRepository;

    private final CustomerRepository customerRepository;

    private final BillRepo billRepository;

    private final BillDetailRepo billDetailRepository;

    private final BillStatusRepo billStatusRepository;

    private final CouponRepo couponRepo;

    private final PromotionDetailRepository promotionDetailRepository;

    private final InvoiceRepository invoiceRepository;

    private final BillStatusRepo billStatusRepo;

    private final JwtService jwtService;

    @Override
    public List<ProductResponse.Product> getExploreOurProducts(Integer page) {
        if (page < 1) {
            page = 1;
        }
        return this.productCustomizeQuery.getExploreOurProducts(page);
    }

    @Override
    public Set<ProductResponse.Product> getBestSellingProducts() {
        return this.productCustomizeQuery.getBestSellingProducts();
    }

    @Override
    public ProductResponse.ProductDetail getProductDetail(Long id) {
        Product product = this.productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        PromotionDetail promotionDetail = this.promotionDetailRepository.findByProductId(product.getId());
        BigDecimal retailPrice = product.getProductDetails().getFirst().getRetailPrice();
        BigDecimal discountPercent = promotionDetail != null
                ? BigDecimal.valueOf(promotionDetail.getPromotion().getPercent()).divide(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        BigDecimal discountPrice = retailPrice.multiply(BigDecimal.valueOf(1).subtract(discountPercent));

        List<ProductResponse.Product> relatedItem = this.productRepository.getRelatedItem(product.getId(), product.getCategory().getName(), product.getBrand().getName(), PageRequest.of(0, 5)).stream().map(s -> {
                    PromotionDetail promotionDetail2 = this.promotionDetailRepository.findByProductId(s.getId());

                    BigDecimal retailPrice2 = s.getProductDetails().getFirst().getRetailPrice();

                    BigDecimal discountPercent2 = promotionDetail2 != null
                            ? BigDecimal.valueOf(promotionDetail2.getPromotion().getPercent()).divide(BigDecimal.valueOf(100))
                            : BigDecimal.ZERO;
                    BigDecimal discountPrice2 = retailPrice2.multiply(BigDecimal.valueOf(1).subtract(discountPercent2));

                    return ProductResponse.Product.builder()
                            .productId(s.getId())
                            .imageUrl(s.getProductImages().getFirst().getImageUrl())
                            .name(s.getName())
                            .retailPrice(s.getProductDetails().getFirst().getRetailPrice())
                            .discountPrice(discountPrice2)
                            .percent(promotionDetail2 != null ? promotionDetail2.getPromotion().getPercent() : null)
                            .expiredDate(promotionDetail2 != null ? promotionDetail2.getPromotion().getEndDate() : null)
                            .rate(4)
                            .rateCount(104)
                            .build();
                }
        ).toList();
        return ProductResponse.ProductDetail.builder()
                .productId(id)
                .imageUrl(product.getProductImages().stream()
                        .map(ProductImage::getImageUrl)
                        .collect(Collectors.toList()))
                .name(product.getName())
                .retailPrice(product.getProductDetails().getFirst().getRetailPrice())
                .discountPrice(product.getProductDetails().getFirst().getRetailPrice().multiply(BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(0.50))))
                .rate(4)
                .rateCount(102)
                //TODO details
                .sizes(this.productDetailRepository.getSizeProduct(product.getId()))
                .colors(this.productDetailRepository.getColorProduct(product.getId()))
                .quantity(this.productDetailRepository.countByProductId(product.getId()))
                .origin(product.getOrigin())
                .category(product.getCategory().getName())
                .material(product.getMaterial().getName())
                .brand(product.getBrand().getName())
                .description(product.getDescription())
                //TODO related items
                .relatedItem(relatedItem)
                .discountPrice(discountPrice)
                .percent(promotionDetail != null ? promotionDetail.getPromotion().getPercent() : null)
                .expiredDate(promotionDetail != null ? promotionDetail.getPromotion().getEndDate() : null)
                .build();
    }

    @Override
    public List<CartResponse.Cart> getCarts(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);
        if (StringUtils.isBlank(authorization)) {
            throw new InvalidDataAccessApiUsageException("Token must be not blank!");
        }
        final String token = authorization.substring("Bearer ".length());
        final String username = this.jwtService.extractUsername(token, ACCESS_TOKEN);
        List<Cart> cart = this.cartRepository.findByUsername(username);
        List<CartResponse.Cart> cartResponses = new ArrayList<>();

        cart.forEach(i -> {
            Optional<ProductDetail> productDetail = productDetailRepository.findById(Long.valueOf(i.getId()));
            CartResponse.ProductCart validProduct = new CartResponse.ProductCart();
            if (productDetail.isPresent()) {
                ProductDetail prd = productDetail.get();
                PromotionDetail promotionDetail = this.promotionDetailRepository.findByProductId(prd.getProduct().getId());
                BigDecimal retailPrice = prd.getRetailPrice();
                BigDecimal discountPercent = promotionDetail != null
                        ? BigDecimal.valueOf(promotionDetail.getPromotion().getPercent()).divide(BigDecimal.valueOf(100))
                        : BigDecimal.ZERO;

                BigDecimal discountPrice = retailPrice.multiply(BigDecimal.valueOf(1).subtract(discountPercent));
                boolean status = prd.getStatus() == ProductStatus.ACTIVE && prd.getProduct().getStatus() == ProductStatus.ACTIVE;
                validProduct.setId(prd.getId());
                validProduct.setStatus(status);
                validProduct.setQuantity(prd.getQuantity());
                validProduct.setPercent(promotionDetail != null ? promotionDetail.getPromotion().getPercent() : null);
                validProduct.setSellPrice(discountPrice);
                validProduct.setMessage(String.format("Product id %d is valid", prd.getId()));
            } else {
                validProduct.setId(Long.parseLong(i.getId()));
                validProduct.setStatus(false);
                validProduct.setQuantity(0);
                validProduct.setMessage(String.format("Product id %s is valid", i.getId()));
            }
            cartResponses.add(CartResponse.Cart.builder()
                    .id(i.getId())
                    .imageUrl(i.getImageUrl())
                    .name(i.getName())
                    .origin(i.getOrigin())
                    .retailPrice(i.getRetailPrice())
                    .quantity(i.getQuantity())
                    .productCart(validProduct)
                    .username(i.getUsername())
                    .build());
        });
        return cartResponses;
    }

    @Override
    public void addToCart(CartRequest.FilterParams req) {
        ProductDetail prd = this.productDetailRepository.findByProductIdAndColorIdAndSizeId(req.getProductId(), req.getColorId(), req.getSizeId()).orElseThrow(() -> new EntityNotFoundException("Sản phẩm này không có sẵn!"));
        Optional<Cart> isAlreadyExists = this.cartRepository.findByIdAndUsername(String.valueOf(prd.getId()), req.getUsername());
        if (isAlreadyExists.isPresent()) {
            Cart updatedCart = isAlreadyExists.get();
            if (prd.getQuantity() <= 0) {
                throw new InvalidDataException("Hết hàng!");
            } else if ((updatedCart.getQuantity() + req.getQuantity()) > prd.getQuantity()) {
                throw new InvalidDataException(String.format("Chỉ còn %d sản phẩm có sẵn!", prd.getQuantity() - updatedCart.getQuantity()));
            }
            updatedCart.setQuantity(updatedCart.getQuantity() + req.getQuantity());
            this.cartRepository.save(updatedCart);
            return;
        }
        if (req.getQuantity() > prd.getQuantity()) {
            throw new InvalidDataException(String.format("Chỉ còn %d sản phẩm có sẵn!", prd.getQuantity()));
        }
        Cart cart = Cart.builder()
                .id(String.valueOf(prd.getId()))
                .imageUrl(prd.getProduct().getProductImages().getFirst().getImageUrl())
                .name(String.format("%s [%s - %s]", prd.getProduct().getName(), prd.getColor().getName(), prd.getSize().getName()))
                .origin(prd.getProduct().getOrigin())
                .retailPrice(prd.getRetailPrice())
                .sellPrice(BigDecimal.valueOf(0))
                .quantity(req.getQuantity())
                .username(req.getUsername())
                .build();
        this.cartRepository.save(cart);
    }

    @Override
    public CartResponse.Cart buyNow(CartRequest.FilterParams req) {
        ProductDetail prd = this.productDetailRepository.findByProductIdAndColorIdAndSizeId(req.getProductId(), req.getColorId(), req.getSizeId()).orElseThrow(() -> new EntityNotFoundException("Sản phẩm này không có sẵn!"));
        PromotionDetail promotionDetail = this.promotionDetailRepository.findByProductId(prd.getProduct().getId());
        BigDecimal discountPercent = promotionDetail != null
                ? BigDecimal.valueOf(promotionDetail.getPromotion().getPercent()).divide(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;
        BigDecimal retailPrice = prd.getRetailPrice();
        BigDecimal discountPrice = retailPrice.multiply(BigDecimal.valueOf(1).subtract(discountPercent));
        boolean status = prd.getStatus() == ProductStatus.ACTIVE && prd.getProduct().getStatus() == ProductStatus.ACTIVE;
        CartResponse.ProductCart productCart = CartResponse.ProductCart.builder()
                .id(prd.getId())
                .status(status)
                .quantity(prd.getQuantity())
                .sellPrice(discountPrice)
                .percent(promotionDetail != null ? promotionDetail.getPromotion().getPercent() : null)
                .build();
        return CartResponse.Cart.builder()
                .id(String.valueOf(prd.getId()))
                .imageUrl(prd.getProduct().getProductImages().getFirst().getImageUrl())
                .name(String.format("%s [%s - %s]", prd.getProduct().getName(), prd.getColor().getName(), prd.getSize().getName()))
                .origin(prd.getProduct().getOrigin())
                .retailPrice(prd.getRetailPrice())
                .quantity(req.getQuantity())
                .username(req.getUsername())
                .productCart(productCart)
                .build();
    }

    @Override
    public void updateCart(CartRequest.Param req) {
        ProductDetail prd = this.productDetailRepository.findById(req.getProductDetailId()).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        Optional<Cart> isAlreadyExists = this.cartRepository.findByIdAndUsername(String.valueOf(req.getProductDetailId()), req.getUsername());
        if (isAlreadyExists.isPresent()) {
            Cart updatedCart = isAlreadyExists.get();
            updatedCart.setQuantity(req.getQuantity());
            if (req.getQuantity() < 1) {
                throw new InvalidDataException("Số lượng phải lớn hơn 0");
            } else if (req.getQuantity() > prd.getQuantity()) {
                throw new InvalidDataException("Bạn đã thêm tối đa số lượng sản phẩm!");
            }
            this.cartRepository.save(updatedCart);
        }
    }

    @Override
    public void deleteCart(String id, String username) {
        Optional<Cart> isAlreadyExists = this.cartRepository.findByIdAndUsername(id, username);
        isAlreadyExists.ifPresent(this.cartRepository::delete);
    }

    @Override
    public UserInfoRes getUserInfo(long id) {
        Customer customer = this.customerRepository.findByUserId(id).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        return UserInfoRes.builder()
                .id(customer.getId())
                .fullName(String.format("%s %s", customer.getLastName(), customer.getFirstName()))
                .phone(customer.getPhoneNumber())
                .email(customer.getUser().getEmail())
                .address(String.format("%s, %s, %s, %s", customer.getCustomerAddress().getStreetName(), customer.getCustomerAddress().getWard(), customer.getCustomerAddress().getDistrict(), customer.getCustomerAddress().getCity()))
                .build();
    }

    @Override
    public long saveBill(BillClientRequest.BillCreate req) {
        Customer customer = this.customerRepository.findById(req.getCustomerId()).orElse(null);
        Coupon couponId = null;
        if (req.getCouponId() != null) {
            couponId = this.couponRepo.findById(req.getCouponId()).orElse(null);
        }
        Bill bill = Bill.builder()
                .code(String.format("HD%s", RandomNumberGenerator.generateEightDigitRandomNumber()))
                .bankCode(req.getBankCode())
                .coupon(couponId)
                .sellerDiscount(req.getSellerDiscount())
                .shipping(req.getShipping())
                .subtotal(req.getSubtotal())
                .total(req.getTotal())
                .paymentMethod(req.getPaymentMethod())
                .message(req.getMessage())
                .customer(customer)
                .billStatus(this.billStatusRepository.findById(2).orElse(null))
                .paymentTime(req.getPaymentMethod() == PaymentMethod.BANK ? new Date() : null)
                .build();
        assert customer != null;
        bill.setCreatedBy(customer.getUser());
        bill.setUpdatedBy(customer.getUser());

        Bill billSave = this.billRepository.save(bill);
        if (req.getCouponId() != null) {
            Coupon coupon = this.couponRepo.findById(req.getCouponId()).orElseThrow(() -> new EntityNotFoundException("Coupon not found"));
            coupon.setQuantity(coupon.getQuantity() - 1);
            coupon.setUsageCount(coupon.getUsageCount() + 1);
            this.couponRepo.save(coupon);
        }

        req.getItems().forEach(item -> {
            ProductDetail prd = this.productDetailRepository.findById(item.getId()).orElseThrow(() -> new EntityNotFoundException("Product not found"));
            if (prd.getQuantity() - item.getQuantity() < 0) {
                billSave.setBillStatus(this.billStatusRepository.findById(7).orElse(null));
                this.billRepository.delete(billSave);
                throw new InvalidDataException("Giao dịch thất bại vui lòng thử lại!");
            }
            this.billDetailRepository.save(BillDetail.builder()
                    .productDetail(prd)
                    .bill(bill)
                    .quantity(item.getQuantity())
                    .retailPrice(item.getRetailPrice())
                    .discountAmount(item.getSellPrice())
                    .totalAmountProduct(item.getSellPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .createAt(new Date())
                    .updateAt(new Date())
                    .build());
            prd.setQuantity(prd.getQuantity() - item.getQuantity());
            this.productDetailRepository.save(prd);
        });

        return billSave.getId();
    }

    @Override
    public PageableResponse getInvoices(InvoiceResponse.Param param) {
        if (param.getPageNo() < 1) {
            param.setPageNo(1);
        }
        return this.invoiceRepository.getAllInvoices(param);
    }

    @Override
    public void cancelInvoice(long id, String message) {
        Bill bill = this.billRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Bill not found"));
        bill.setBillStatus(this.billStatusRepository.findById(7).orElse(null));
        bill.setMessage(message);
        bill.getBillDetails().forEach(detail -> {
            ProductDetail productDetail = this.productDetailRepository.findById(detail.getProductDetail().getId()).orElseThrow(() -> new EntityNotFoundException("Product not found"));
            assert productDetail != null;
            productDetail.setQuantity(productDetail.getQuantity() + detail.getQuantity());
            this.productDetailRepository.save(productDetail);
        });
        this.billRepository.save(bill);
    }

    @Override
    public List<InvoiceResponse.InvoiceStatus> getInvoiceStatuses() {
        return this.billStatusRepo.findAll().stream().filter(i -> i.getId() != 1).map(i ->
                InvoiceResponse.InvoiceStatus.builder()
                        .id(i.getId())
                        .name(i.getName())
                        .status(i.getStatus())
                        .build()
        ).toList();
    }

    @Override
    public PageableResponse productFilters(ProductRequests.ParamFilters param) {
        if (param.getPageNo() < 1) {
            param.setPageNo(1);
        }
        return this.productCustomizeQuery.getProductsFilters(param);
    }

}
