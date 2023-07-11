package com.cydeo.accounting_app.dto;

import com.cydeo.accounting_app.enums.Months;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Long id;
    private Integer year;
    private Months month;
    private LocalDate paymentDate;
    private int amount;
    private Boolean isPaid;
    private String companyStripeId;
    private String description;
    private CompanyDTO company;


}
