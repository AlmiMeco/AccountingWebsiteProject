package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.InvoiceProductDTO;
import com.cydeo.accounting_app.dto.ProductDTO;
import com.cydeo.accounting_app.entity.Product;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.ProductRepository;
import com.cydeo.accounting_app.service.LoggedInUserService;
import com.cydeo.accounting_app.service.InvoiceProductService;
import com.cydeo.accounting_app.service.ProductService;
import com.cydeo.accounting_app.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends LoggedInUserService implements ProductService {

    private final ProductRepository productRepository;
    private final InvoiceProductService invoiceProductService;

    public ProductServiceImpl(InvoiceProductService invoiceProductService,SecurityService securityService, MapperUtil mapperUtil, ProductRepository productRepository) {
        super(securityService, mapperUtil);
        this.productRepository = productRepository;
        this.invoiceProductService = invoiceProductService;
    }

    @Override
    public ProductDTO findById(Long id) {
        Optional<Product> byId = productRepository.findById(id);
        Product product = byId.orElseThrow(() -> new NoSuchElementException("Product not found"));
        return mapperUtil.convert(product, new ProductDTO());
    }

    @Override
    public List<ProductDTO> getProductList() {
        List<Product> productList = productRepository.findByCategoryCompany(getCompany());
        return productList.stream()
                .sorted(Comparator.comparing((Product product) -> product.getCategory().getDescription())
                        .thenComparing(Product::getName))
                .map(product -> mapperUtil.convert(product, new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> findAllProductsWithCategoryId(Long categoryId) {
        return productRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(product -> mapperUtil.convert(product, new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> findAllProductsByCompany() {
        return productRepository.findAllByCompanyId(getCompany().getId()).stream()
                .map(product -> mapperUtil.convert(product,new ProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isProductNameExist(ProductDTO productDTO) {
        Product existingProduct = productRepository.findByNameIgnoreCaseAndCategoryCompany(productDTO.getName(), getCompany());
        if (existingProduct == null) {
            return false;
        }
        return !existingProduct.getId().equals(productDTO.getId());
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        Product product = mapperUtil.convert(productDTO, new Product());
//        product.setQuantityInStock(0);
        return mapperUtil.convert(productRepository.save(product), new ProductDTO());
    }

    @Override
    public ProductDTO update(Long id, ProductDTO productDTO) {
        productDTO.setId(id);
        Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product " + productDTO.getName() + "not found"));
        productDTO.setQuantityInStock(product.getQuantityInStock());
        product = productRepository.save(mapperUtil.convert(productDTO, new Product()));
        return mapperUtil.convert(product, new ProductDTO());
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    @Override
    public boolean checkProductQuantity(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        List<InvoiceProductDTO> invoiceProducts = invoiceProductService.findAllInvoiceProductsByProductId(id);
        return invoiceProducts.size() > 0 || product.getQuantityInStock() > 0;
    }
}
