package com.cydeo.accounting_app.dto;

import com.cydeo.accounting_app.enums.InvoiceStatus;
import com.cydeo.accounting_app.enums.InvoiceType;
import java.time.LocalDate;
import java.math.BigDecimal;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InvoiceDTO {

    private Long id;
    private String invoiceNo;
    private InvoiceStatus invoiceStatus;
    private InvoiceType invoiceType;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private CompanyDTO company;
    @NotNull()
    private ClientVendorDTO clientVendor;
    private BigDecimal price;
    private BigDecimal tax;
    private BigDecimal total;
}
