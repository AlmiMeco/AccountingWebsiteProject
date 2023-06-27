package com.cydeo.accounting_app.entity;

import com.cydeo.accounting_app.enums.ClientVendorType;
import com.cydeo.accounting_app.enums.ClientVendorTypeEnum;
import lombok.*;

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
    private ClientVendorType clientVendorTypeEnum;
    @OneToOne
    @Column(name = "address_id")
    private Address address;
    @Column(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

}