package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.*;
import com.cydeo.accounting_app.entity.ClientVendor;
import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.entity.Invoice;
import com.cydeo.accounting_app.entity.InvoiceProduct;
import com.cydeo.accounting_app.enums.InvoiceStatus;
import com.cydeo.accounting_app.enums.InvoiceType;
import com.cydeo.accounting_app.exception.InvoiceNotFoundException;
import com.cydeo.accounting_app.exception.InvoiceProductNotFoundException;
import com.cydeo.accounting_app.exception.ProductNotFoundException;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.InvoiceRepository;
import com.cydeo.accounting_app.service.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl extends LoggedInUserService implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;

    public InvoiceServiceImpl(SecurityService securityService, MapperUtil mapperUtil, InvoiceRepository invoiceRepository, @Lazy InvoiceProductService invoiceProductService, @Lazy ProductService productService) {
        super(securityService, mapperUtil);
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
    }

    @Override
    public InvoiceDTO findById(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(
                () -> new InvoiceNotFoundException("This Invoice with id " + invoiceId + " does not exist"));
        return mapperUtil.convert(invoice, new InvoiceDTO());
    }

    @Override
    public List<InvoiceDTO> listAllInvoicesByType(InvoiceType type) {
        return invoiceRepository.findAllByInvoiceTypeAndCompany(type, getCompany().id).stream()
                .sorted(Comparator.comparing(Invoice::getId))
                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDTO()))
                .map(invoiceDTO -> calculateInvoice(invoiceDTO.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void saveInvoiceByType(InvoiceDTO invoiceDTO, InvoiceType type) {
        Invoice invoice = mapperUtil.convert(invoiceDTO, new Invoice());
        invoice.setInvoiceType(type);
        invoice.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoice.setCompany(getCompany());
        invoiceRepository.save(invoice);
    }

    @Override
    public void deleteInvoiceById(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(
                () -> new InvoiceNotFoundException("This Invoice with id " + invoiceId + " does not exist"));
        invoice.setIsDeleted(true);
        invoiceProductService.deleteInvoiceProductsByInvoiceId(invoiceId);
        invoiceRepository.save(invoice);
    }

    @Override
    public void approveInvoiceById(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(
                () -> new InvoiceNotFoundException("This Invoice with id " + invoiceId + " does not exist"));

        if(invoice.getInvoiceType().equals(InvoiceType.PURCHASE)){
            purchaseInvoiceApproval(invoice);
        }else{
            isValidSalesApprove(invoiceId);
            salesInvoiceApproval(invoice);
        }
        /**
         * When invoice approves , the date of invoice changes to approval date
         */
        invoice.setDate(LocalDate.now());
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoiceRepository.save(invoice);
    }

    private void isValidSalesApprove(Long invoiceId) {
        List<InvoiceProductDTO> allInvoiceProductsDTO  = invoiceProductService.findAllInvoiceProductsByInvoiceId(invoiceId)
                .stream()
                .distinct()
                .toList();
        for (InvoiceProductDTO each : allInvoiceProductsDTO) {
            int productQuantityInStock = each.getProduct().getQuantityInStock();
            int productQuantityInInvoice = invoiceProductService.findAllInvoiceProductsByInvoiceId(invoiceId)
                    .stream()
                    .filter(ip-> ip.getProduct().getId().equals(each.getProduct().getId()))
                    .map(InvoiceProductDTO::getQuantity)
                    .reduce(Integer::sum)
                    .orElse(0);

            if (productQuantityInStock<productQuantityInInvoice) {
                throw new ProductNotFoundException("This sale cannot be completed due to insufficient quantity of the product");
            }
        }
    }


    private void purchaseInvoiceApproval(Invoice invoice) {
        Long invoiceId = invoice.getId();
        /**
         * If invoice approved it needs to increase or decrease quantity stock of product.
         * Code below handles it.
         */
        invoiceProductService.findAllInvoiceProductsByInvoiceId(invoiceId).stream()
                .map(invoiceProductDTO -> {
                    Long productId = invoiceProductDTO.getProduct().getId();
                    Integer productQuantity = invoiceProductDTO.getQuantity();
                    ProductDTO productDTO = productService.findById(productId);
                    Integer productCurrentQuantity = productDTO.getQuantityInStock();
                    productDTO.setQuantityInStock(productCurrentQuantity + productQuantity);
                    return productDTO;
                })
                .forEach(productService::save);
        /**
        * Set RemainingQty  = Quantity for new Purchase Invoice
        * Set ProfitLoss = 0 forever
        */
        invoiceProductService.findAllInvoiceProductsByInvoiceId(invoiceId)
                .stream()
                .peek(invoiceProductDTO -> {
                    invoiceProductDTO.setRemainingQty(invoiceProductDTO.getQuantity());
                    invoiceProductDTO.setProfitLoss(new BigDecimal(BigInteger.ZERO));
                })
                .forEach(invoiceProductDTO -> invoiceProductService.saveInvoiceProduct(invoiceProductDTO, invoiceId));

    }
    private void salesInvoiceApproval(Invoice invoice) {
        Long invoiceId = invoice.getId();
        /**
         * If invoice approved it needs to increase or decrease quantity stock of product.
         * Code below handles it.
         */

        invoiceProductService.findAllInvoiceProductsByInvoiceId(invoiceId).stream()
                .map(invoiceProductDTO -> {
                    Long productId = invoiceProductDTO.getProduct().getId();
                    Integer productQuantity = invoiceProductDTO.getQuantity();
                    ProductDTO productDTO = productService.findById(productId);
                    Integer productCurrentQuantity = productDTO.getQuantityInStock();
                        productDTO.setQuantityInStock(productCurrentQuantity - productQuantity);
                    return productDTO;
                })
                .forEach(productService::save);
        /**
         * When we have approved sales invoice we should:
         * Calculate profitLoss for invoice
         * Reduce RemainingQty of products in Purchase invoices
         */
        invoiceProductService.calculationProfitLossAllInvoiceProducts(invoiceId);
    }


    @Override
    public InvoiceDTO createInvoice(InvoiceType type) {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        /**
         * The code below handle invoiceNo for new invoice
         */
        List<String> listInvoiceNoByType = invoiceRepository.findMaxInvoiceIdByType(type.getValue()
                .toUpperCase(), getCompany().id);
        int value = listInvoiceNoByType.stream().map(s -> Integer.parseInt(s.substring(2)))
                .sorted((a, b) -> a > b ? -1 : a == b ? 0 : 1)
                .findFirst().orElse(0);
        value++;
        String InvoiceNo = type.getValue().charAt(0) +
                "-" + ((value < 100) ? "0" : "") + ((value < 10) ? "0" : "") + value;
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
        return mapperUtil.convert(company, new CompanyDTO());
    }

    @Override
    public InvoiceDTO calculateInvoice(Long invoiceId) {
        /**
         * This method makes calculation to fill Invoice DTO : tax,price,total
         */
        InvoiceDTO invoiceDTO = findById(invoiceId);
        List<InvoiceProductDTO> invoiceProductDTOS = invoiceProductService.findAllInvoiceProductsByInvoiceId(invoiceId);

        BigDecimal invoicePrice = BigDecimal.ZERO;
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

    @Override
    public boolean existsByClientVendorId(Long id) {
        return invoiceRepository.existsByCompanyAndClientVendorId(getCompany(), id);
    }

    @Override
    public List<InvoiceDTO> listAllApprovedInvoices() {
        return invoiceRepository.findAllByCompanyAndInvoiceStatus(getCompany(), InvoiceStatus.APPROVED).stream()
                .sorted(Comparator.comparing(Invoice::getId).reversed())
                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDTO()))
                .map(invoiceDTO -> calculateInvoice(invoiceDTO.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> listAllInvoicesForDashboardChart(InvoiceType invoiceType) {
        /**
         * This list used in dashboard service to calculate charts
         */
        return invoiceRepository.findAllByCompanyAndInvoiceStatusAndInvoiceType(getCompany(), InvoiceStatus.APPROVED, invoiceType).stream()
                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDTO()))
                .map(invoiceDTO -> calculateInvoice(invoiceDTO.getId()))
                .collect(Collectors.toList());
    }


    public List<InvoiceDTO> list3LastApprovalInvoicesForDashboard() {
        /**
         * This method returns information for dashboard list, only 3 last invoices.
         * It filtered by Company.
         */
        List<InvoiceDTO> threeLastInvoices = new ArrayList<>();
        int sizeApprovalInvoicesList = listAllApprovedInvoices().size();
        for (int i = 0; i < (Math.min(sizeApprovalInvoicesList, 3)); i++)
            threeLastInvoices.add(listAllApprovedInvoices().get(i));
        return threeLastInvoices;
    }

    @Override
    public void updateInvoice(Long invoiceId, InvoiceDTO invoiceDTO) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(
                () -> new InvoiceNotFoundException("This Invoice with id " + invoiceId + " does not exist")
        );
        invoice.setClientVendor(
                mapperUtil.convert(invoiceDTO.getClientVendor(), new ClientVendor()));
        invoiceRepository.save(invoice);
    }

}
