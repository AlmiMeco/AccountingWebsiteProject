package com.cydeo.accounting_app.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "categories")
@Where(clause = "is_deleted = false")
public class Category extends BaseEntity {

        private String description;
        @ManyToOne(fetch = FetchType.LAZY)
        private Company company;

}
