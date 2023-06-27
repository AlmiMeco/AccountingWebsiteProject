package com.cydeo.accounting_app.entity;

import com.cydeo.accounting_app.enums.ClientVendorType;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "clients_vendors")
public class ClientVendor extends BaseEntity {
    private String clientVendorName;
    private String phone;
    private String website;
    @Enumerated(EnumType.STRING)
    private ClientVendorType clientVendorType;
    @OneToOne
    @Column(name = "address_id")
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.PERSIST})
    private Address address;
    @Column(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

}
