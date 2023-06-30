package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.CompanyDTO;
import com.cydeo.accounting_app.dto.InvoiceDTO;
import com.cydeo.accounting_app.dto.ProductDTO;
import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.entity.Invoice;
import com.cydeo.accounting_app.enums.InvoiceStatus;
import com.cydeo.accounting_app.enums.InvoiceType;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.InvoiceRepository;
import com.cydeo.accounting_app.service.InvoiceService;
import com.cydeo.accounting_app.service.LoggedInUserService;
import com.cydeo.accounting_app.service.SecurityService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl extends LoggedInUserService implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl(SecurityService securityService, MapperUtil mapperUtil, InvoiceRepository invoiceRepository) {
        super(securityService, mapperUtil);
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public InvoiceDTO findById(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(
                () -> new RuntimeException("This Invoice does not exist"));
        if(invoice.isDeleted){
            throw new RuntimeException("The invoice has been deleted");
        }
        return mapperUtil.convert(invoice,new InvoiceDTO());
    }

    @Override
    public List<InvoiceDTO> listAllInvoicesByType(InvoiceType type) {
        return invoiceRepository.findAllByInvoiceType(type).stream()
                .map(invoice -> mapperUtil.convert(invoice,new InvoiceDTO()))
                .collect(Collectors.toList());
    }
    @Override
    public void saveInvoiceByType(InvoiceDTO invoiceDTO,InvoiceType type) {
        Invoice invoice = mapperUtil.convert(invoiceDTO, new Invoice());
        invoice.setInvoiceType(type);
        invoice.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceRepository.save(invoice);
    }

    @Override
    public void deleteInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Invoice does not exist"));
        invoice.setIsDeleted(true);
        invoiceRepository.save(invoice);
    }

    @Override
    public void addNewProductToInvoice(Long invoiceId, ProductDTO productDTO) {

    }

    @Override
    public void approveInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Invoice does not exist"));
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoiceRepository.save(invoice);
    }

    @Override
    public InvoiceDTO createInvoice(InvoiceType type) {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        List<String> listInvoiceNoByType = invoiceRepository.findMaxInvoiceIdByType(type.getValue().toUpperCase());
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
    public String findLastInvoiceId() {
        return String.valueOf(invoiceRepository.findMaxNumberInvoiceId());
    }

    @Override
    public CompanyDTO getCurrentCompany() {
        Company company = getCompany();
        return mapperUtil.convert(company,new CompanyDTO());
    }


}
