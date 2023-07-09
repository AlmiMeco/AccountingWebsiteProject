package com.cydeo.accounting_app.service.implementation;

import com.cydeo.accounting_app.dto.CompanyDTO;
import com.cydeo.accounting_app.dto.PaymentDTO;
import com.cydeo.accounting_app.entity.Company;
import com.cydeo.accounting_app.mapper.MapperUtil;
import com.cydeo.accounting_app.repository.PaymentRepository;
import com.cydeo.accounting_app.service.LoggedInUserService;
import com.cydeo.accounting_app.service.PaymentService;
import com.cydeo.accounting_app.service.SecurityService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl extends LoggedInUserService implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(SecurityService securityService, MapperUtil mapperUtil, PaymentRepository paymentRepository) {
        super(securityService, mapperUtil);
        this.paymentRepository = paymentRepository;
    }


    @Override
    public List<PaymentDTO> listAllPaymentsByYear(int year) {

       var loggedCompany =  mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company());

        return  paymentRepository.findAllByCompanyAndYear(loggedCompany, year)
                .stream()
                .map(i -> mapperUtil.convert(i, new PaymentDTO()))
                .sorted(Comparator.comparing(PaymentDTO::getMonth))
                .collect(Collectors.toList());

    }
}
