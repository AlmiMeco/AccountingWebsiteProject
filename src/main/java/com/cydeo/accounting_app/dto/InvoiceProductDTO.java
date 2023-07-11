package com.cydeo.accounting_app.dto;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class InvoiceProductDTO {

    private Long id;
    @Range(min=1, max=100)
    @NotNull
    private Integer quantity;
    @Min(value = 1)
    @NotNull
    private BigDecimal price;
    @Range(min=0, max=20)
    @NotNull
    private Integer tax;
    private BigDecimal total;
    private BigDecimal profitLoss;
    private Integer remainingQty;
    private InvoiceDTO invoice;
    @NotNull()
    private ProductDTO product;
}
