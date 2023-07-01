package com.cydeo.accounting_app.repository;

import com.cydeo.accounting_app.entity.Category;
import com.cydeo.accounting_app.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * This method for getting all categories just of current company
     * @param company will pass as a parameter
     * @return categories of current company will be returned
     */
    List<Category> findAllByCompany(Company company);

    Category findByDescriptionAndCompany(String description, Company company);

}
