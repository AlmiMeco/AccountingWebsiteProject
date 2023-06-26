package com.cydeo.accounting_app.entity;

import com.cydeo.accounting_app.enums.ClientVendorTypeEnum;

import javax.persistence.*;

@Entity
@Table(name = "clients_vendors")
public class ClientVendorEntity extends BaseEntity {
    private String clientVendorName;
    private String phone;
    private String website;
    @Enumerated(EnumType.STRING)
    private ClientVendorTypeEnum clientVendorTypeEnum;
    @OneToOne
    @Column(name = "address_id")
    private Address address;
    @Column(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

}
