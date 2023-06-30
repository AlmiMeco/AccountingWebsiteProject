package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCategoryId(Long categoryId);

    List<Product> findByCategoryCompany(Company company);

    Product findByNameAndCategoryCompany(String productName, Company company);

}
