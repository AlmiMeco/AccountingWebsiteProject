package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.InvoiceDTO;
import com.cydeo.accounting_app.dto.InvoiceProductDTO;
import com.cydeo.accounting_app.entity.Invoice;
import com.cydeo.accounting_app.entity.InvoiceProduct;
import com.cydeo.accounting_app.enums.InvoiceStatus;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.InvoiceProductRepository;
import com.cydeo.accounting_app.service.InvoiceProductService;
import com.cydeo.accounting_app.service.InvoiceService;
import com.cydeo.accounting_app.service.LoggedInUserService;
import com.cydeo.accounting_app.service.SecurityService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceProductServiceImpl extends LoggedInUserService implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;
    private final InvoiceService invoiceService;

    public InvoiceProductServiceImpl(SecurityService securityService, MapperUtil mapperUtil, InvoiceProductRepository invoiceProductRepository, InvoiceService invoiceService) {
        super(securityService, mapperUtil);
        this.invoiceProductRepository = invoiceProductRepository;
        this.invoiceService = invoiceService;
    }

    @Override
    public InvoiceProductDTO findById(Long invoiceProductId) {
        InvoiceProduct invoiceProduct= invoiceProductRepository.findById(invoiceProductId).orElseThrow(
                () -> new RuntimeException("This InvoiceProduct does not exist")
        );
        if(invoiceProduct.isDeleted){
            throw new RuntimeException("The InvoiceProduct has been deleted");
        }
        return mapperUtil.convert(invoiceProduct,new InvoiceProductDTO());
    }

    @Override
    public List<InvoiceProductDTO> findAllInvoiceProductsByInvoiceId(Long invoiceId) {
        InvoiceDTO invoiceDTO = invoiceService.findById(invoiceId);
        Invoice invoice = mapperUtil.convert(invoiceDTO,new Invoice());
        /**
         * The code below makes calculation for InvoiceProductDTOs which belongs to Invoice.
         * Set total field.
         */
        return invoiceProductRepository.findAllByInvoice(invoice)
                .stream()
                .map(invoiceProduct -> mapperUtil.convert(invoiceProduct,new InvoiceProductDTO()))
                .peek(invoiceProductDTO -> {
                    BigDecimal totalWithoutTax = invoiceProductDTO.getPrice().multiply(BigDecimal.valueOf(invoiceProductDTO.getQuantity()));;
                    BigDecimal tax = BigDecimal.valueOf(invoiceProductDTO.getTax()).movePointLeft(2);
                    BigDecimal totalTax = totalWithoutTax.multiply(tax);
                    BigDecimal totalWithTax = totalWithoutTax.add(totalTax);
                    totalWithTax = totalWithTax.setScale(2, RoundingMode.CEILING);
                    invoiceProductDTO.setTotal(totalWithTax);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void saveInvoiceProduct(InvoiceProductDTO invoiceProductDTO,Long invoiceId) {
        InvoiceDTO invoiceDTO = invoiceService.findById(invoiceId);
        InvoiceProduct invoiceProduct = mapperUtil.convert(invoiceProductDTO,new InvoiceProduct());
        Invoice invoice = mapperUtil.convert(invoiceDTO,new Invoice());
        invoiceProduct.setInvoice(invoice);
        invoiceProductRepository.save(invoiceProduct);
    }

    @Override
    public void deleteInvoiceProductById(Long invoiceProductId) {
        InvoiceProduct invoiceProduct = invoiceProductRepository.findById(invoiceProductId).orElseThrow(
                () -> new RuntimeException("InvoiceProduct does not exist"));
        invoiceProduct.setIsDeleted(true);
        invoiceProductRepository.save(invoiceProduct);
    }

    @Override
    public void deleteInvoiceProductsByInvoiceId(Long invoiceId) {
        List<InvoiceProduct> invoiceProduct = invoiceProductRepository.findAllByInvoiceId(invoiceId);
        for(int i = 0 ;i<invoiceProduct.size();i++)
            deleteInvoiceProductById(invoiceId);
    }

    @Override
    public List<InvoiceProductDTO> findAllInvoiceProductsByProductId(Long productId) {
        return invoiceProductRepository.findByProductId(productId)
                .stream()
                .map(invoiceProduct -> mapperUtil.convert(invoiceProduct, new InvoiceProductDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isStockNotEnough(InvoiceProductDTO invoiceProductDTO) {
        if(invoiceProductDTO.getQuantity()==null||
           invoiceProductDTO.getProduct().getQuantityInStock()==null)
            return false;
        return invoiceProductDTO.getQuantity()>invoiceProductDTO.getProduct().getQuantityInStock();
    }

    @Override
    public List<InvoiceProductDTO> findAllInvoiceProductsByInvoiceStatusApproved(InvoiceStatus invoiceStatus) {
        return invoiceProductRepository.findAllInvoiceProductsByInvoiceStatus
                (InvoiceStatus.APPROVED).stream().sorted(Comparator.comparing(invoiceProduct ->
                invoiceProduct.getInvoice().getDate())).map(invoiceProduct -> mapperUtil.convert(invoiceProduct,
                new InvoiceProductDTO())).collect(Collectors.toList());
    }


}

