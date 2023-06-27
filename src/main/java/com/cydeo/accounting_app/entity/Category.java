package com.cydeo.accounting_app.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

        private String description;
        @ManyToOne
        private Company company;

}
