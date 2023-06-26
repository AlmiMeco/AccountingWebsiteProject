package com.cydeo.accounting_app.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Where(clause = "is_deleted=false")
public class User extends BaseEntity {

    @Column(unique = true)
    private String username;

    private String password;

    private String firstname;

    private String lastname;

    private String phone;

    private boolean enabled;

    @ManyToOne
    private Role role;

//    @ManyToOne
//    private Company company;

}
