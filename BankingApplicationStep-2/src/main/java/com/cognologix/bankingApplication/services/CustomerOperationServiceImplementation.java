package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dto.CustomerDto;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.globleObjectLists.DataSouceForCustomer;
import com.cognologix.bankingApplication.globleObjectLists.DataSoucrce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerOperationServiceImplementation implements CustomerOperationService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer createNewCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Double getAccountBalance(Long accountNumber) {
        return bankAccountRepository.findByAccountNumberEquals(accountNumber).getBalance();
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


}
