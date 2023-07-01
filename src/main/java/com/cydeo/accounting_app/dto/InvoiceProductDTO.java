package com.cydeo.accounting_app.dto;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InvoiceProductDTO {

    private Long id;
    @Range(min=1, max=100, message = "Quantity cannot be greater than 100 or less than 1")
    private Integer quantity;
    @DecimalMin(value = "1", inclusive = false, message = "Price should be at least $1")
    private BigDecimal price;
    @Range(min=0, max=20, message = "Tax should be between 0% and 20%")
    private Integer tax;
    private BigDecimal total;
    private BigDecimal profitLoss;
    private Integer remainingQty;
    private InvoiceDTO invoice;
    @NotNull
    private ProductDTO product;

}
