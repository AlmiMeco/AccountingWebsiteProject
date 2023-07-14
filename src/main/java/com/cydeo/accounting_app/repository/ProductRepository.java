package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByCategoryId(Long categoryId);
    @Query("SELECT p " +
            "FROM Product p " +
            "JOIN Category c " +
            "ON c.id = p.category.id " +
            "WHERE c.company.id = ?1")
    List<Product> findAllByCompanyId(Long companyId);

    List<Product> findByCategoryCompany(Company company);

    Product findByNameAndCategoryCompany(String productName, Company company);

    Product findByNameIgnoreCaseAndCategoryCompany(String productName, Company company);
}
