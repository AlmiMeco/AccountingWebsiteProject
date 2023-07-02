package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.*;
import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.entity.Invoice;
import com.cydeo.accounting_app.enums.InvoiceStatus;
import com.cydeo.accounting_app.enums.InvoiceType;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.InvoiceRepository;
import com.cydeo.accounting_app.service.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl extends LoggedInUserService implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;

    public InvoiceServiceImpl(SecurityService securityService, MapperUtil mapperUtil, InvoiceRepository invoiceRepository, @Lazy InvoiceProductService invoiceProductService,@Lazy ProductService productService) {
        super(securityService, mapperUtil);
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
    }

    @Override
    public InvoiceDTO findById(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(
                () -> new RuntimeException("This Invoice does not exist"));
        if(invoice.isDeleted){
            throw new RuntimeException("The invoice has been deleted");
        }
        return mapperUtil.convert(invoice,new InvoiceDTO());
    }

    @Override
    public List<InvoiceDTO> listAllInvoicesByType(InvoiceType type) {
        return invoiceRepository.findAllByInvoiceTypeAndCompany(type, getCompany().id).stream()
                .sorted(Comparator.comparing(Invoice::getId).reversed())
                .map(invoice -> mapperUtil.convert(invoice,new InvoiceDTO()))
                .map(invoiceDTO -> calculateInvoice(invoiceDTO.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void saveInvoiceByType(InvoiceDTO invoiceDTO,InvoiceType type) {
        Invoice invoice = mapperUtil.convert(invoiceDTO, new Invoice());
        invoice.setInvoiceType(type);
        invoice.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoice.setCompany(getCompany());
        invoiceRepository.save(invoice);
    }

    @Override
    public void deleteInvoiceById(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(
                () -> new RuntimeException("Invoice does not exist"));
        invoice.setIsDeleted(true);
        invoiceProductService.deleteInvoiceProductsByInvoiceId(invoiceId);
        invoiceRepository.save(invoice);
    }

    @Override
    public void approveInvoiceById(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(
                () -> new RuntimeException("Invoice does not exist"));
        /**
         * If invoice approved it needs to increase or decrease quantity stock of product.
         * Code below handles it.
         */
        invoiceProductService.findAllInvoiceProductsByInvoiceId(invoiceId).stream()
                .map( invoiceProductDTO -> {
                    Long productId = invoiceProductDTO.getProduct().getId();
                    Integer IPproductQuantity = invoiceProductDTO.getQuantity();
                    ProductDTO productDTO = productService.findById(productId);
                    Integer productCurrentQuantity = productDTO.getQuantityInStock();
                    if(invoice.getInvoiceType().equals(InvoiceType.PURCHASE)){
                        productDTO.setQuantityInStock(productCurrentQuantity+IPproductQuantity);
                    }else {
                        productDTO.setQuantityInStock(productCurrentQuantity-IPproductQuantity);
                    }
                    return productDTO;
                })
                .forEach(productService::save);
        /**
         * When invoice approves , the date of invoice changes to approval date
         */
        invoice.setDate(LocalDate.now());
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoiceRepository.save(invoice);
    }

    @Override
    public InvoiceDTO createInvoice(InvoiceType type) {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        /**
         * The code below handle invoiceNo for new invoice
         */
        List<String> listInvoiceNoByType = invoiceRepository.findMaxInvoiceIdByType(type.getValue().toUpperCase(), getCompany().id);
        int value;
        if(listInvoiceNoByType.size()==0){
            value=0;
        }else {
            String lastInvoiceNo = listInvoiceNoByType.get(listInvoiceNoByType.size()-1);
            value =Integer.parseInt(lastInvoiceNo.substring(2));
            value++;
        }
        String InvoiceNo = type.getValue().charAt(0)+
                "-"+((value<100)?"0":"") +((value<10)?"0":"")+value;
        invoiceDTO.setInvoiceNo(InvoiceNo);
        invoiceDTO.setDate(LocalDate.now());
        return invoiceDTO;
    }

    @Override
    public String findLastInvoiceId(InvoiceType type) {
        /**
         * This method needs int controller in update endpoint to update the last created invoice.
         */
        return String.valueOf(invoiceRepository.findMaxNumberInvoiceIdByCompanyIdAndType(type, getCompany().id));
    }

    @Override
    public CompanyDTO getCurrentCompany() {
        /**
         * This method I use to send current company information to my controller
         */
        Company company = getCompany();
        return mapperUtil.convert(company,new CompanyDTO());
    }

    @Override
    public InvoiceDTO calculateInvoice(Long invoiceId) {
        /**
         * This method makes calculation to fill Invoice DTO : tax,price,total
         */
        InvoiceDTO invoiceDTO = findById(invoiceId);
        List<InvoiceProductDTO> invoiceProductDTOS = invoiceProductService.findAllInvoiceProductsByInvoiceId(invoiceId);
        BigDecimal invoicePrice = BigDecimal.ZERO;;
        BigDecimal invoiceTotal = BigDecimal.ZERO;
        for (InvoiceProductDTO invoiceProductDTO : invoiceProductDTOS) {
            BigDecimal quantity = BigDecimal.valueOf(invoiceProductDTO.getQuantity());
            invoicePrice = invoicePrice.add(invoiceProductDTO.getPrice().multiply(quantity));
            invoiceTotal = invoiceTotal.add(invoiceProductDTO.getTotal());
        }
        invoiceDTO.setTax(invoiceTotal.subtract(invoicePrice));
        invoiceDTO.setPrice(invoicePrice);
        invoiceDTO.setTotal(invoiceTotal);

        return invoiceDTO;
    }

    @Override
    public InvoiceDTO getInvoiceForPrint(Long invoiceId) {
        /**
         * Information for print page
         */
        return calculateInvoice(invoiceId);
    }




}
