package com.cydeo.accounting_app.entity;

import com.cydeo.accounting_app.enums.Months;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment extends BaseEntity {

    private int year;

    private int amount;

    private LocalDate paymentDate;

    private Boolean isPaid;

    private String companyStripeId;

    @Enumerated(value = EnumType.STRING)
    private Months month;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
}
