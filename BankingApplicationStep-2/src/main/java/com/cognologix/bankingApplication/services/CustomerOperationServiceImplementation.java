package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.exceptions.AccountNotAvailableException;
import com.cognologix.bankingApplication.exceptions.CustomerAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerOperationServiceImplementation implements CustomerOperationService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    //creating new customer
    @Override
    public Customer createNewCustomer(Customer customer) {
        //if the customer identity element is already exist then throws exception
        try {
            customerRepository.findAll().stream().forEach(customerFromList -> {
                if (customerFromList.getAdharNumber().equals(customer.getAdharNumber())) {
                    throw new CustomerAlreadyExistException("Customer is already exist by same adhar number....");
                } else if (customerFromList.getPanCardNumber().equals(customer.getPanCardNumber())) {
                    throw new CustomerAlreadyExistException("Customer is already exist by same PanCard number....");
                } else if (customerFromList.getEmailId().equals(customer.getEmailId())) {
                    throw new CustomerAlreadyExistException("Customer is already exist by same email....");
                }
            });
            return customerRepository.save(customer);
        } catch (CustomerAlreadyExistException exception) {
            throw new CustomerAlreadyExistException(exception.getMessage());
        }
    }

    //get account balance by account number
    @Override
    public Double getAccountBalance(Long accountNumber) {
        try {
            Account accountAvailable = bankAccountRepository.findByAccountNumberEquals(accountNumber);
            if (accountAvailable == null) {
                throw new AccountNotAvailableException("Account for given account number does not exist...");
            }
            return bankAccountRepository.findByAccountNumberEquals(accountNumber).getBalance();
        } catch (AccountNotAvailableException exception) {
            throw new AccountNotAvailableException(exception.getMessage());
        }

    }

    //returns all the customers
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


}
