package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.example.enums.InvoiceStatus;

@Getter
@Setter
@Entity
@Table(name = "bill_status")
public class BillStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 55)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InvoiceStatus status;

    @Column(name = "description")
    private String description;


}