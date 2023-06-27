package com.cydeo.accounting_app.entity;

import com.cydeo.accounting_app.enums.ProductUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    private String name;
    private int quantityInStock;
    private int lowLimitAlert;
    @Enumerated(value = EnumType.STRING)
    private ProductUnit productUnit;
    @ManyToOne
    private Category category;

}
