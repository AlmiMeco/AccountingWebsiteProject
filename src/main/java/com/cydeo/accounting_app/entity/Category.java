package com.cydeo.accounting_app.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Category extends BaseEntity {

    String description;
    @ManyToOne
    Company company;

}
