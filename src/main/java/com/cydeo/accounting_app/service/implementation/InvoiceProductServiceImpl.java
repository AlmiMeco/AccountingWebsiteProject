package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.InvoiceDTO;
import com.cydeo.accounting_app.dto.InvoiceProductDTO;
import com.cydeo.accounting_app.entity.Invoice;
import com.cydeo.accounting_app.entity.InvoiceProduct;
import com.cydeo.accounting_app.entity.Product;
import com.cydeo.accounting_app.enums.InvoiceStatus;
import com.cydeo.accounting_app.enums.InvoiceType;
import com.cydeo.accounting_app.exception.InvoiceProductNotFoundException;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.InvoiceProductRepository;
import com.cydeo.accounting_app.service.InvoiceProductService;
import com.cydeo.accounting_app.service.InvoiceService;
import com.cydeo.accounting_app.service.LoggedInUserService;
import com.cydeo.accounting_app.service.SecurityService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
                () -> new InvoiceProductNotFoundException("This InvoiceProduct with id " + invoiceProductId +" does not exist")
        );
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

        //price*quantity*(tax/100)
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
                () -> new InvoiceProductNotFoundException("This InvoiceProduct with id " + invoiceProductId +" does not exist"));
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
    public boolean isStockNotEnough(InvoiceProductDTO invoiceProductDTO,Long invoiceId) {
        /**
         * Check if we have enough products to sell
         */
        Long productId = invoiceProductDTO.getProduct().getId();

        int currentProductQuantityInInvoice = invoiceProductRepository.findAllByInvoiceId(invoiceId)
                .stream().filter(invoiceProduct -> invoiceProduct.getProduct().getId().equals(productId))
                .map(InvoiceProduct::getQuantity)
                .reduce(Integer::sum)
                .orElse(0);

        int currentQuantity = currentProductQuantityInInvoice+invoiceProductDTO.getQuantity();
        return currentQuantity>invoiceProductDTO.getProduct().getQuantityInStock();
    }

    @Override
    public List<InvoiceProductDTO> findAllInvoiceProductsByInvoiceStatus(InvoiceStatus invoiceStatus) {
        return invoiceProductRepository.findAllInvoiceProductsByInvoiceStatus(
                InvoiceStatus.APPROVED).stream().sorted(Comparator.comparing(invoiceProduct ->
                invoiceProduct.getInvoice().getDate())).map(invoiceProduct -> mapperUtil.convert(invoiceProduct,
                new InvoiceProductDTO())).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceProductDTO> findAllInvoiceProductsByStatusAndCompany() {
        return invoiceProductRepository.findByInvoiceInvoiceStatusAndInvoiceCompany(
                InvoiceStatus.APPROVED, getCompany()).stream().sorted(Comparator.comparing
                (InvoiceProduct::getId).reversed()).map(invoiceProduct ->
                mapperUtil.convert(invoiceProduct,
                new InvoiceProductDTO())).collect(Collectors.toList());
    }


    @Override
    public String productLowLimitAlert(InvoiceProductDTO invoiceProductDTO, Long invoiceId) {
        return invoiceProductRepository.findAllByInvoiceId(invoiceId).stream()
                .filter(invoiceProduct -> invoiceProduct.getProduct().getId().equals(invoiceProductDTO.getProduct().getId()))
                .filter(invoiceProduct ->{
                    int productAlertQty = invoiceProduct.getProduct().getLowLimitAlert();
                    int productsInInvoice = invoiceProductRepository.findAllByInvoiceId(invoiceId)
                            .stream().filter(invoiceProduct1 -> invoiceProduct1.getProduct().equals(invoiceProduct.getProduct()))
                            .map(InvoiceProduct::getQuantity)
                            .reduce(Integer::sum).orElse(0);
                    int productsInStock = invoiceProduct.getProduct().getQuantityInStock();
                    return productsInStock - productsInInvoice < productAlertQty;
                }).map(invoiceProduct -> invoiceProduct.getProduct().getName())
                .findAny().orElse("");
    }

    @Override
    public void calculationProfitLossAllInvoiceProducts(Long invoiceId) {
        /**
         * Get all invoiceProducts from approving Invoice
         */
        List<InvoiceProduct> allSalesInvoiceProducts = invoiceProductRepository.findAllByInvoiceId(invoiceId);

        for(InvoiceProduct salesInvoiceProduct: allSalesInvoiceProducts ){
            int salesProductQty = salesInvoiceProduct.getQuantity();
            while (salesProductQty>0){
                InvoiceProduct purInvoiceProduct = invoiceProductRepository
                        .findFirstByProductAndRemainingQtyGreaterThanOrderById(salesInvoiceProduct.getProduct(),
                                0);
                BigDecimal profitLoss,currentProfitLoss;
                BigDecimal purchasePrice = purInvoiceProduct.getPrice();
                /**
                 * Getting Prices with tax
                 */
                purchasePrice = purchasePrice.add(purchasePrice.multiply(BigDecimal.valueOf(purInvoiceProduct.getTax()).movePointLeft(2)));
                BigDecimal salesPrice = salesInvoiceProduct.getPrice();
                salesPrice = salesPrice.add(salesPrice.multiply(BigDecimal.valueOf(salesInvoiceProduct.getTax()).movePointLeft(2)));
                if(purInvoiceProduct.getRemainingQty()>=salesProductQty){
                    profitLoss = salesPrice.subtract(purchasePrice).multiply(BigDecimal.valueOf(salesProductQty));
                    currentProfitLoss = salesInvoiceProduct.getProfitLoss()==null?
                            BigDecimal.ZERO:salesInvoiceProduct.getProfitLoss();
                    salesProductQty=0;
                }else{
                    int actualProductQty = purInvoiceProduct.getRemainingQty();
                    BigDecimal priceDiff = salesPrice.subtract(purchasePrice);
                    profitLoss = priceDiff.multiply(BigDecimal.valueOf(actualProductQty));
                    currentProfitLoss = salesInvoiceProduct.getProfitLoss()==null?
                            BigDecimal.ZERO:salesInvoiceProduct.getProfitLoss();
                    salesProductQty-=actualProductQty;
                }
                BigDecimal totalProfitLoss = profitLoss.add(currentProfitLoss);
                salesInvoiceProduct.setProfitLoss(totalProfitLoss);
                purInvoiceProduct.setRemainingQty(0);
                invoiceProductRepository.save(salesInvoiceProduct);
                invoiceProductRepository.save(purInvoiceProduct);
            }
        }
    }
}

