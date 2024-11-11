package com.example.repositories.customQuery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.example.dto.requests.common.ProductParamFilter;
import com.example.dto.requests.common.ProductParamFilter2;
import com.example.dto.requests.productRequests.ProductRequests;
import com.example.dto.response.PageableResponse;
import com.example.dto.response.clients.product.ProductResponse;
import com.example.dto.response.productResponse.ProductDetailResponse2;
import com.example.enums.ProductStatus;
import com.example.model.Product;
import com.example.model.ProductDetail;
import com.example.model.ProductImage;
import com.example.model.PromotionDetail;
import com.example.repositories.products.ProductDetailRepository;
import com.example.repositories.promotions.PromotionDetailRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.dto.requests.productRequests.ProductRequests.SortBy.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductCustomizeQuery {
    @PersistenceContext
    private EntityManager entityManager;

    private final ProductDetailRepository productDetailRepository;

    private final PromotionDetailRepository promotionDetailRepository;

    private static final String LIKE_FORMAT = "%%%s%%";

    public PageableResponse getAllProducts(ProductParamFilter param) {
        StringBuilder sql = new StringBuilder("SELECT prd FROM Product prd WHERE prd.isDeleted = false");
        if (StringUtils.hasLength(param.getKeyword())) {
            sql.append(" AND lower(prd.name) like lower(:keyword)");
        }

        if (param.getStatus() != ProductStatus.ALL && param.getStatus() != ProductStatus.OUT_OF_STOCK) {
            sql.append(" AND prd.status = :status");
        } else if (param.getStatus() == ProductStatus.OUT_OF_STOCK) {
            sql.append(" AND ((SELECT coalesce(sum(d.quantity), 0) FROM ProductDetail d WHERE d.product.id = prd.id AND d.status = 'ACTIVE') < 1)");
        }

        if (StringUtils.hasLength(param.getCategory())) {
            sql.append(" AND prd.category.name like :category");
        }

        if (StringUtils.hasLength(param.getBrand())) {
            sql.append(" AND prd.brand.name like :brand");
        }

        if (StringUtils.hasLength(param.getMaterial())) {
            sql.append(" AND prd.material.name like :material");
        }

        if (StringUtils.hasLength(param.getOrigin())) {
            sql.append(" AND prd.origin like :origin");
        }

        sql.append(" ORDER BY prd.id DESC");

        TypedQuery<Product> query = entityManager.createQuery(sql.toString(), Product.class);
        if (StringUtils.hasLength(param.getKeyword())) {
            query.setParameter("keyword", String.format(LIKE_FORMAT, param.getKeyword().trim()));
        }
        if (param.getStatus() != ProductStatus.ALL && param.getStatus() != ProductStatus.OUT_OF_STOCK) {
            query.setParameter("status", param.getStatus());
        }
        if (StringUtils.hasLength(param.getCategory())) {
            query.setParameter("category", param.getCategory());
        }

        if (StringUtils.hasLength(param.getBrand())) {
            query.setParameter("brand", param.getBrand());
        }

        if (StringUtils.hasLength(param.getMaterial())) {
            query.setParameter("material", param.getMaterial());
        }

        if (StringUtils.hasLength(param.getOrigin())) {
            query.setParameter("origin", param.getOrigin());
        }

        query.setFirstResult((param.getPageNo() - 1) * param.getPageSize());
        query.setMaxResults(param.getPageSize());

        List<com.example.dto.response.productResponse.ProductResponse> data = query.getResultList().stream().map(this::convertToProductResponse).toList();

        // TODO count product
        StringBuilder countPage = new StringBuilder("SELECT count(prd) FROM Product prd WHERE prd.isDeleted = false");
        if (StringUtils.hasLength(param.getKeyword())) {
            countPage.append(" AND lower(prd.name) like lower(:keyword)");
        }

        if (param.getStatus() != ProductStatus.ALL && param.getStatus() != ProductStatus.OUT_OF_STOCK) {
            countPage.append(" AND prd.status = :status");
        } else if (param.getStatus() == ProductStatus.OUT_OF_STOCK) {
            countPage.append(" AND ((SELECT coalesce(sum(d.quantity), 0) FROM ProductDetail d WHERE d.product.id = prd.id AND d.status = 'ACTIVE') < 1)");
        }
        if (StringUtils.hasLength(param.getCategory())) {
            countPage.append(" AND prd.category.name like :category");
        }

        if (StringUtils.hasLength(param.getBrand())) {
            countPage.append(" AND prd.brand.name like :brand");
        }

        if (StringUtils.hasLength(param.getMaterial())) {
            countPage.append(" AND prd.material.name like :material");
        }

        if (StringUtils.hasLength(param.getOrigin())) {
            countPage.append(" AND prd.origin like :origin");
        }

        TypedQuery<Long> countQuery = entityManager.createQuery(countPage.toString(), Long.class);
        if (StringUtils.hasLength(param.getKeyword())) {
            countQuery.setParameter("keyword", String.format(LIKE_FORMAT, param.getKeyword().trim()));
        }
        if (param.getStatus() != ProductStatus.ALL && param.getStatus() != ProductStatus.OUT_OF_STOCK) {
            countQuery.setParameter("status", param.getStatus());
        }
        if (StringUtils.hasLength(param.getCategory())) {
            countQuery.setParameter("category", param.getCategory());
        }

        if (StringUtils.hasLength(param.getBrand())) {
            countQuery.setParameter("brand", param.getBrand());
        }

        if (StringUtils.hasLength(param.getMaterial())) {
            countQuery.setParameter("material", param.getMaterial());
        }

        if (StringUtils.hasLength(param.getOrigin())) {
            countQuery.setParameter("origin", param.getOrigin());
        }
        Long totalElements = countQuery.getSingleResult();
        Pageable pageable = PageRequest.of(param.getPageNo() - 1, param.getPageSize());
        Page<?> page = new PageImpl<>(data, pageable, totalElements);
        return PageableResponse.builder()
                .pageNumber(param.getPageNo())
                .pageNo(param.getPageNo())
                .pageSize(param.getPageSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements()) // Not required
                .content(data)
                .build();
    }

    public PageableResponse getAllProductArchives(ProductParamFilter param) {
        StringBuilder sql = new StringBuilder("SELECT prd FROM Product prd WHERE prd.isDeleted = true");
        if (StringUtils.hasLength(param.getKeyword())) {
            sql.append(" AND lower(prd.name) like lower(:keyword)");
        }

        if (param.getStatus() != ProductStatus.ALL && param.getStatus() != ProductStatus.OUT_OF_STOCK) {
            sql.append(" AND prd.status = :status");
        } else if (param.getStatus() == ProductStatus.OUT_OF_STOCK) {
            sql.append(" AND ((SELECT coalesce(sum(d.quantity), 0) FROM ProductDetail d WHERE d.product.id = prd.id AND d.status = 'ACTIVE') < 1)");
        }

        if (StringUtils.hasLength(param.getCategory())) {
            sql.append(" AND prd.category.name like :category");
        }

        if (StringUtils.hasLength(param.getBrand())) {
            sql.append(" AND prd.brand.name like :brand");
        }

        if (StringUtils.hasLength(param.getMaterial())) {
            sql.append(" AND prd.material.name like :material");
        }

        if (StringUtils.hasLength(param.getOrigin())) {
            sql.append(" AND prd.origin like :origin");
        }

        sql.append(" ORDER BY prd.updateAt DESC");

        TypedQuery<Product> query = entityManager.createQuery(sql.toString(), Product.class);
        if (StringUtils.hasLength(param.getKeyword())) {
            query.setParameter("keyword", String.format(LIKE_FORMAT, param.getKeyword().trim()));
        }
        if (param.getStatus() != ProductStatus.ALL && param.getStatus() != ProductStatus.OUT_OF_STOCK) {
            query.setParameter("status", param.getStatus());
        }
        if (StringUtils.hasLength(param.getCategory())) {
            query.setParameter("category", param.getCategory());
        }

        if (StringUtils.hasLength(param.getBrand())) {
            query.setParameter("brand", param.getBrand());
        }

        if (StringUtils.hasLength(param.getMaterial())) {
            query.setParameter("material", param.getMaterial());
        }

        if (StringUtils.hasLength(param.getOrigin())) {
            query.setParameter("origin", param.getOrigin());
        }

        query.setFirstResult((param.getPageNo() - 1) * param.getPageSize());
        query.setMaxResults(param.getPageSize());

        List<com.example.dto.response.productResponse.ProductResponse> data = query.getResultList().stream().map(this::convertToProductResponse).toList();

        // TODO count product
        StringBuilder countPage = new StringBuilder("SELECT count(prd) FROM Product prd WHERE prd.isDeleted = true");
        if (StringUtils.hasLength(param.getKeyword())) {
            countPage.append(" AND lower(prd.name) like lower(:keyword)");
        }

        if (param.getStatus() != ProductStatus.ALL && param.getStatus() != ProductStatus.OUT_OF_STOCK) {
            countPage.append(" AND prd.status = :status");
        } else if (param.getStatus() == ProductStatus.OUT_OF_STOCK) {
            countPage.append(" AND ((SELECT coalesce(sum(d.quantity), 0) FROM ProductDetail d WHERE d.product.id = prd.id AND d.status = 'ACTIVE') < 1)");
        }
        if (StringUtils.hasLength(param.getCategory())) {
            countPage.append(" AND prd.category.name like :category");
        }

        if (StringUtils.hasLength(param.getBrand())) {
            countPage.append(" AND prd.brand.name like :brand");
        }

        if (StringUtils.hasLength(param.getMaterial())) {
            countPage.append(" AND prd.material.name like :material");
        }

        if (StringUtils.hasLength(param.getOrigin())) {
            countPage.append(" AND prd.origin like :origin");
        }

        TypedQuery<Long> countQuery = entityManager.createQuery(countPage.toString(), Long.class);
        if (StringUtils.hasLength(param.getKeyword())) {
            countQuery.setParameter("keyword", String.format(LIKE_FORMAT, param.getKeyword().trim()));
        }
        if (param.getStatus() != ProductStatus.ALL && param.getStatus() != ProductStatus.OUT_OF_STOCK) {
            countQuery.setParameter("status", param.getStatus());
        }
        if (StringUtils.hasLength(param.getCategory())) {
            countQuery.setParameter("category", param.getCategory());
        }

        if (StringUtils.hasLength(param.getBrand())) {
            countQuery.setParameter("brand", param.getBrand());
        }

        if (StringUtils.hasLength(param.getMaterial())) {
            countQuery.setParameter("material", param.getMaterial());
        }

        if (StringUtils.hasLength(param.getOrigin())) {
            countQuery.setParameter("origin", param.getOrigin());
        }
        Long totalElements = countQuery.getSingleResult();
        Pageable pageable = PageRequest.of(param.getPageNo() - 1, param.getPageSize());
        Page<?> page = new PageImpl<>(data, pageable, totalElements);
        return PageableResponse.builder()
                .pageNumber(param.getPageNo())
                .pageNo(param.getPageNo())
                .pageSize(param.getPageSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements()) // Not required
                .content(data)
                .build();
    }

    public PageableResponse getAllProductDetails(ProductParamFilter2 param) {
        StringBuilder sql = new StringBuilder("SELECT prd FROM ProductDetail prd WHERE prd.product.isDeleted = false AND prd.status = 'ACTIVE' AND prd.product.status = 'ACTIVE' AND prd.quantity > 0");
        if (StringUtils.hasLength(param.getKeyword())) {
            sql.append(" AND lower(prd.product.name) like lower(:keyword)");
        }

        if (StringUtils.hasLength(param.getCategory())) {
            sql.append(" AND prd.product.category.name like :category");
        }

        if (StringUtils.hasLength(param.getBrand())) {
            sql.append(" AND prd.product.brand.name like :brand");
        }

        if (StringUtils.hasLength(param.getMaterial())) {
            sql.append(" AND prd.product.material.name like :material");
        }

        if (StringUtils.hasLength(param.getOrigin())) {
            sql.append(" AND prd.product.origin like :origin");
        }

        if (StringUtils.hasLength(param.getColor())) {
            sql.append(" AND prd.color.name like :color");
        }

        if (StringUtils.hasLength(param.getSize())) {
            sql.append(" AND prd.size.name like :size");
        }

        sql.append(" ORDER BY prd.product.id DESC");

        TypedQuery<ProductDetail> query = entityManager.createQuery(sql.toString(), ProductDetail.class);
        if (StringUtils.hasLength(param.getKeyword())) {
            query.setParameter("keyword", String.format(LIKE_FORMAT, param.getKeyword().trim()));
        }

        if (StringUtils.hasLength(param.getCategory())) {
            query.setParameter("category", param.getCategory());
        }

        if (StringUtils.hasLength(param.getBrand())) {
            query.setParameter("brand", param.getBrand());
        }

        if (StringUtils.hasLength(param.getMaterial())) {
            query.setParameter("material", param.getMaterial());
        }

        if (StringUtils.hasLength(param.getOrigin())) {
            query.setParameter("origin", param.getOrigin());
        }

        if (StringUtils.hasLength(param.getColor())) {
            query.setParameter("color", param.getColor());
        }

        if (StringUtils.hasLength(param.getSize())) {
            query.setParameter("size", param.getSize());
        }

        query.setFirstResult((param.getPageNo() - 1) * param.getPageSize());
        query.setMaxResults(param.getPageSize());

        List<ProductDetailResponse2> data = query.getResultList().stream().map(item -> {
            PromotionDetail promotionDetail = this.promotionDetailRepository.findByProductId(item.getProduct().getId());
            BigDecimal retailPrice = item.getRetailPrice();
            BigDecimal discountPercent = promotionDetail != null
                    ? BigDecimal.valueOf(promotionDetail.getPromotion().getPercent()).divide(BigDecimal.valueOf(100))
                    : BigDecimal.ZERO;

            BigDecimal discountPrice = retailPrice.multiply(BigDecimal.valueOf(1).subtract(discountPercent));

            return ProductDetailResponse2.builder()
                    .id(item.getId())
                    .productName(String.format("%s [%s - %s]", item.getProduct().getName(), item.getSize().getName(), item.getColor().getName()))
                    .imageUrl(convertToUrl(item.getProduct().getProductImages()))
                    .brand(item.getProduct().getBrand().getName())
                    .category(item.getProduct().getCategory().getName())
                    .material(item.getProduct().getMaterial().getName())
                    .color(item.getColor().getName())
                    .size(item.getSize().getName())
                    .origin(item.getProduct().getOrigin())
                    .price(item.getRetailPrice())
                    .sellPrice(discountPrice)
                    .percent(promotionDetail != null ? promotionDetail.getPromotion().getPercent() : null)
                    .expiredDate(promotionDetail != null ? promotionDetail.getPromotion().getEndDate() : null)
                    .quantity(item.getQuantity())
                    .build();
        }).toList();

        // TODO count product
        StringBuilder countPage = new StringBuilder("SELECT count(prd) FROM ProductDetail prd WHERE prd.product.isDeleted = false AND prd.status = 'ACTIVE' AND prd.product.status = 'ACTIVE' AND prd.quantity > 0");
        if (StringUtils.hasLength(param.getKeyword())) {
            countPage.append(" AND lower(prd.product.name) like lower(:keyword)");
        }

        if (StringUtils.hasLength(param.getCategory())) {
            countPage.append(" AND prd.product.category.name like :category");
        }

        if (StringUtils.hasLength(param.getBrand())) {
            countPage.append(" AND prd.product.brand.name like :brand");
        }

        if (StringUtils.hasLength(param.getMaterial())) {
            countPage.append(" AND prd.product.material.name like :material");
        }

        if (StringUtils.hasLength(param.getOrigin())) {
            countPage.append(" AND prd.product.origin like :origin");
        }

        if (StringUtils.hasLength(param.getColor())) {
            countPage.append(" AND prd.color.name like :color");
        }

        if (StringUtils.hasLength(param.getSize())) {
            countPage.append(" AND prd.size.name like :size");
        }

        TypedQuery<Long> countQuery = entityManager.createQuery(countPage.toString(), Long.class);
        if (StringUtils.hasLength(param.getKeyword())) {
            countQuery.setParameter("keyword", String.format(LIKE_FORMAT, param.getKeyword().trim()));
        }

        if (StringUtils.hasLength(param.getCategory())) {
            countQuery.setParameter("category", param.getCategory());
        }

        if (StringUtils.hasLength(param.getBrand())) {
            countQuery.setParameter("brand", param.getBrand());
        }

        if (StringUtils.hasLength(param.getMaterial())) {
            countQuery.setParameter("material", param.getMaterial());
        }

        if (StringUtils.hasLength(param.getOrigin())) {
            countQuery.setParameter("origin", param.getOrigin());
        }

        if (StringUtils.hasLength(param.getColor())) {
            countQuery.setParameter("color", param.getColor());
        }

        if (StringUtils.hasLength(param.getSize())) {
            countQuery.setParameter("size", param.getSize());
        }
        Long totalElements = countQuery.getSingleResult();
        Pageable pageable = PageRequest.of(param.getPageNo() - 1, param.getPageSize());
        Page<?> page = new PageImpl<>(data, pageable, totalElements);
        return PageableResponse.builder()
                .pageNumber(param.getPageNo())
                .pageNo(param.getPageNo())
                .pageSize(param.getPageSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements()) // Not required
                .content(data)
                .build();
    }

    public List<ProductResponse.Product> getExploreOurProducts(Integer page) {
        StringBuilder query = new StringBuilder("SELECT prd FROM Product prd WHERE prd.status = 'ACTIVE' AND prd.isDeleted = false");
        query.append(" AND ((SELECT coalesce(sum(d.quantity), 0) FROM ProductDetail d WHERE d.product.id = prd.id AND d.status = 'ACTIVE') > 0)");
        query.append(" ORDER BY prd.updateAt DESC");
        TypedQuery<Product> execute = entityManager.createQuery(query.toString(), Product.class);
        execute.setFirstResult((page - 1) * 10);
        execute.setMaxResults(12);

        return execute.getResultList().stream()
                .map(s -> {
                            PromotionDetail promotionDetail = this.promotionDetailRepository.findByProductId(s.getId());
                            BigDecimal retailPrice = s.getProductDetails().getFirst().getRetailPrice();
                            BigDecimal discountPercent = promotionDetail != null
                                    ? BigDecimal.valueOf(promotionDetail.getPromotion().getPercent()).divide(BigDecimal.valueOf(100))
                                    : BigDecimal.ZERO;

                            BigDecimal discountPrice = retailPrice.multiply(BigDecimal.valueOf(1).subtract(discountPercent));

                            return ProductResponse.Product.builder()
                                    .productId(s.getId())
                                    .imageUrl(s.getProductImages().getFirst().getImageUrl())
                                    .name(s.getName())
                                    .retailPrice(s.getProductDetails().getFirst().getRetailPrice())
                                    .discountPrice(discountPrice)
                                    .rate(4)
                                    .rateCount(104)
                                    .percent(promotionDetail != null ? promotionDetail.getPromotion().getPercent() : null)
                                    .expiredDate(promotionDetail != null ? promotionDetail.getPromotion().getEndDate() : null)
                                    .build();
                        }
                ).toList();
    }

    public Set<ProductResponse.Product> getBestSellingProducts() {
        StringBuilder query = new StringBuilder("SELECT prd FROM Product prd WHERE prd.status = 'ACTIVE' AND prd.isDeleted = false");
        query.append(" AND ((SELECT coalesce(sum(d.quantity), 0) FROM ProductDetail d WHERE d.product.id = prd.id AND d.status = 'ACTIVE') > 0)");
        query.append(" ORDER BY prd.updateAt DESC");
        TypedQuery<Product> execute = entityManager.createQuery(query.toString(), Product.class);
        execute.setMaxResults(6);

        return execute.getResultList().stream()
                .map(s -> {
                            PromotionDetail promotionDetail = this.promotionDetailRepository.findByProductId(s.getId());
                            BigDecimal retailPrice = s.getProductDetails().getFirst().getRetailPrice();
                            BigDecimal discountPercent = promotionDetail != null
                                    ? BigDecimal.valueOf(promotionDetail.getPromotion().getPercent()).divide(BigDecimal.valueOf(100))
                                    : BigDecimal.ZERO;

                            BigDecimal discountPrice = retailPrice.multiply(BigDecimal.valueOf(1).subtract(discountPercent));
                            return ProductResponse.Product.builder()
                                    .productId(s.getId())
                                    .imageUrl(s.getProductImages().getFirst().getImageUrl())
                                    .name(s.getName())
                                    .retailPrice(s.getProductDetails().getFirst().getRetailPrice())
                                    .discountPrice(discountPrice)
                                    .rate(4)
                                    .rateCount(104)
                                    .percent(promotionDetail != null ? promotionDetail.getPromotion().getPercent() : null)
                                    .expiredDate(promotionDetail != null ? promotionDetail.getPromotion().getEndDate() : null)
                                    .build();
                        }
                ).collect(Collectors.toSet());
    }

    public PageableResponse getProductsFilters(ProductRequests.ParamFilters param) {
        StringBuilder query = new StringBuilder("SELECT prd FROM Product prd WHERE prd.status = 'ACTIVE' AND prd.isDeleted = false");
        // Default query
        query.append(" AND ((SELECT coalesce(sum(d.quantity), 0) FROM ProductDetail d WHERE d.product.id = prd.id AND d.status = 'ACTIVE') > 0)");

        if (StringUtils.hasLength(param.getKeyword())) {
            query.append(" AND lower(prd.name) like lower(:keyword)");
        }

        if (!param.getCategoryIds().isEmpty()) {
            String categoryIds = param.getCategoryIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            query.append(String.format(" AND prd.category.id IN (%s)", categoryIds));
        }

        if (!param.getBrandIds().isEmpty()) {
            String brandIds = param.getBrandIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            query.append(String.format(" AND prd.brand.id IN (%s)", brandIds));
        }

        if (!param.getMaterialIds().isEmpty()) {
            String materialIds = param.getCategoryIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            query.append(String.format(" AND prd.material.id IN (%s)", materialIds));
        }

        if (param.getMinPrice() != null || param.getMaxPrice() != null) {
            query.append(" AND EXISTS (SELECT 1 FROM ProductDetail d WHERE d.product.id = prd.id AND d.status = 'ACTIVE'");

            if (param.getMinPrice() != null) {
                query.append(" AND d.retailPrice >= :minPrice");
            }
            if (param.getMaxPrice() != null) {
                query.append(" AND d.retailPrice <= :maxPrice");
            }

            query.append(")");
        }

        if (param.getSortBy() == PRICE_ASC) {
            query.append(" ORDER BY (SELECT MIN(d.retailPrice) FROM ProductDetail d WHERE d.product.id = prd.id AND d.status = 'ACTIVE') ASC");
        } else if (param.getSortBy() == PRICE_DESC) {
            query.append(" ORDER BY (SELECT MAX(d.retailPrice) FROM ProductDetail d WHERE d.product.id = prd.id AND d.status = 'ACTIVE') DESC");
        } else if (param.getSortBy() == CREATED_AT) {
            query.append(" ORDER BY prd.createAt DESC");
        } else {
            query.append(" ORDER BY prd.updateAt DESC");
        }

        TypedQuery<Product> execute = entityManager.createQuery(query.toString(), Product.class);
        if (StringUtils.hasLength(param.getKeyword())) {
            execute.setParameter("keyword", String.format(LIKE_FORMAT, param.getKeyword().trim()));
        }

        if (param.getMinPrice() != null || param.getMaxPrice() != null) {
            if (param.getMinPrice() != null) {
                execute.setParameter("minPrice", param.getMinPrice());
            }
            if (param.getMaxPrice() != null) {
                execute.setParameter("maxPrice", param.getMinPrice());
            }
        }

        execute.setFirstResult((param.getPageNo() - 1) * param.getPageSize());
        execute.setMaxResults(param.getPageSize());

        List<ProductResponse.Product> data = execute.getResultList().stream()
                .map(s -> {
                            PromotionDetail promotionDetail = this.promotionDetailRepository.findByProductId(s.getId());
                            BigDecimal retailPrice = s.getProductDetails().getFirst().getRetailPrice();
                            BigDecimal discountPercent = promotionDetail != null
                                    ? BigDecimal.valueOf(promotionDetail.getPromotion().getPercent()).divide(BigDecimal.valueOf(100))
                                    : BigDecimal.ZERO;

                            BigDecimal discountPrice = retailPrice.multiply(BigDecimal.valueOf(1).subtract(discountPercent));
                            return ProductResponse.Product.builder()
                                    .productId(s.getId())
                                    .imageUrl(s.getProductImages().getFirst().getImageUrl())
                                    .name(s.getName())
                                    .retailPrice(s.getProductDetails().getFirst().getRetailPrice())
                                    .discountPrice(discountPrice)
                                    .rate(4)
                                    .rateCount(104)
                                    .percent(promotionDetail != null ? promotionDetail.getPromotion().getPercent() : null)
                                    .expiredDate(promotionDetail != null ? promotionDetail.getPromotion().getEndDate() : null)
                                    .build();
                        }
                ).toList();

        StringBuilder countPage = new StringBuilder("SELECT count(prd) FROM Product prd WHERE prd.status = 'ACTIVE' AND prd.isDeleted = false");
        countPage.append(" AND ((SELECT coalesce(sum(d.quantity), 0) FROM ProductDetail d WHERE d.product.id = prd.id AND d.status = 'ACTIVE') > 0)");

        if (StringUtils.hasLength(param.getKeyword())) {
            countPage.append(" AND lower(prd.name) like lower(:keyword)");
        }

        if (!param.getCategoryIds().isEmpty()) {
            String categoryIds = param.getCategoryIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            countPage.append(String.format(" AND prd.category.id IN (%s)", categoryIds));
        }

        if (!param.getBrandIds().isEmpty()) {
            String brandIds = param.getBrandIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            countPage.append(String.format(" AND prd.brand.id IN (%s)", brandIds));
        }

        if (!param.getMaterialIds().isEmpty()) {
            String materialIds = param.getCategoryIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            countPage.append(String.format(" AND prd.material.id IN (%s)", materialIds));
        }

        if (param.getMinPrice() != null || param.getMaxPrice() != null) {
            countPage.append(" AND EXISTS (SELECT 1 FROM ProductDetail d WHERE d.product.id = prd.id AND d.status = 'ACTIVE'");

            if (param.getMinPrice() != null) {
                countPage.append(" AND d.retailPrice >= :minPrice");
            }
            if (param.getMaxPrice() != null) {
                countPage.append(" AND d.retailPrice <= :maxPrice");
            }

            countPage.append(")");
        }

        if (param.getSortBy() == PRICE_ASC) {
            countPage.append(" ORDER BY (SELECT MIN(d.retailPrice) FROM ProductDetail d WHERE d.product.id = prd.id AND d.status = 'ACTIVE') ASC");
        } else if (param.getSortBy() == PRICE_DESC) {
            countPage.append(" ORDER BY (SELECT MAX(d.retailPrice) FROM ProductDetail d WHERE d.product.id = prd.id AND d.status = 'ACTIVE') DESC");
        } else if (param.getSortBy() == CREATED_AT) {
            countPage.append(" ORDER BY prd.createAt DESC");
        } else {
            countPage.append(" ORDER BY prd.updateAt DESC");
        }
        TypedQuery<Long> countQuery = entityManager.createQuery(countPage.toString(), Long.class);
        if (StringUtils.hasLength(param.getKeyword())) {
            countQuery.setParameter("keyword", String.format(LIKE_FORMAT, param.getKeyword().trim()));
        }

        if (param.getMinPrice() != null || param.getMaxPrice() != null) {
            if (param.getMinPrice() != null) {
                countQuery.setParameter("minPrice", param.getMinPrice());
            }
            if (param.getMaxPrice() != null) {
                countQuery.setParameter("maxPrice", param.getMinPrice());
            }
        }
        Long totalElements = countQuery.getSingleResult();
        Pageable pageable = PageRequest.of(param.getPageNo() - 1, param.getPageSize());
        Page<?> page = new PageImpl<>(data, pageable, totalElements);
        return PageableResponse.builder()
                .pageNumber(param.getPageNo())
                .pageNo(param.getPageNo())
                .pageSize(param.getPageSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements()) // Not required
                .content(data)
                .build();
    }

    private com.example.dto.response.productResponse.ProductResponse convertToProductResponse(Product product) {
        return com.example.dto.response.productResponse.ProductResponse.builder()
                .id(product.getId())
                .imageUrl(convertToUrl(product.getProductImages()))
                .name(product.getName())
                .description(product.getDescription())
                .status(product.getStatus())
                .category(product.getCategory().getName())
                .brand(product.getBrand().getName())
                .material(product.getMaterial().getName())
                .origin(product.getOrigin())
                .createdBy(product.getCreatedBy().getUsername())
                .updatedBy(product.getUpdatedBy().getUsername())
                .createdAt(product.getCreateAt())
                .updatedAt(product.getUpdateAt())
                .productQuantity(this.productDetailRepository.countByProductId(product.getId()))
                .build();
    }

    private List<String> convertToUrl(List<ProductImage> images) {
        return images.stream()
                .map(ProductImage::getImageUrl)
                .collect(Collectors.toList());
    }
}
