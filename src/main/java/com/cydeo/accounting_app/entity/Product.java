package com.cydeo.accounting_app.entity;

import com.cydeo.accounting_app.enums.ProductUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    String name;
    int quantityInStock;
    int lowLimitAlert;
    ProductUnit productUnit;
    @ManyToOne
    Category category;

}
