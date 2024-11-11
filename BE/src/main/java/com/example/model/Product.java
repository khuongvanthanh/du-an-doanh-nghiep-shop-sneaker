package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import com.example.enums.ProductStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product  extends AbstractEntity<Long> implements Serializable {

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    @Column(name = "status")
    private ProductStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "material_id")
    private Material material;

    @Column(name = "origin", length = 30)
    private String origin;

    @OneToMany(mappedBy = "product")
    private List<ProductDetail> productDetails = new ArrayList<>();


    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages = new ArrayList<>();

}