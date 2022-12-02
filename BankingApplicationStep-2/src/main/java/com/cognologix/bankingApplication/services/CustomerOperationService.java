package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dto.responsesForCustomerOperations.BalanceInquiryResponse;
import com.cognologix.bankingApplication.dto.responsesForCustomerOperations.CreateCustomerResponse;
import com.cognologix.bankingApplication.dto.responsesForCustomerOperations.CustomerUpdateResponse;
import com.cognologix.bankingApplication.dto.responsesForCustomerOperations.GetAllCustomerResponse;
import com.cognologix.bankingApplication.entities.Customer;

public interface CustomerOperationService {
    CreateCustomerResponse createNewCustomer(Customer customer);
    BalanceInquiryResponse getAccountBalance(Long accountNumber);
    GetAllCustomerResponse getAllCustomers();

    CustomerUpdateResponse updateCustomer(Customer customer);
}
