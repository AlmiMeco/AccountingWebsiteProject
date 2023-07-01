package com.cydeo.accounting_app.repository;

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
            "JOIN Company co " +
            "ON c.company.id = co.id " +
            "WHERE co.id = ?1")
    List<Product> findAllByCompanyId(Long companyId);

}
