package com.cognologix.bankingApplication.controllers;

import com.cognologix.bankingApplication.dto.TransactionDto;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.services.BankOperationsService;
import com.cognologix.bankingApplication.services.CustomerOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerServiceController {

    //customer related operations
    @Autowired
    CustomerOperationService customerOperationService;

    @Autowired
    BankOperationsService bankOperationsSevice;

    //create and return created customer by giving parameter to customer
    @PostMapping("/createCustomer")
    public ResponseEntity<Customer> createNewAccount(@Valid @RequestBody Customer customer) {
        Customer createdCustomer = customerOperationService.createNewCustomer(customer);
        return new ResponseEntity<Customer>(createdCustomer, HttpStatus.CREATED);
    }

    //checking balance by giving account number
    @GetMapping("/balanceInquiry")
    public ResponseEntity<String> checkBalance(@Valid @PathParam(value = "accountNumber") Long accountNumber){
        Double currentBalance=customerOperationService.getAccountBalance(accountNumber);
        return new ResponseEntity<String>("Current balance is ---> "+currentBalance,HttpStatus.OK);
    }

    //get list of BankTransaction details
    @GetMapping("/statementOfTransaction")
    public ResponseEntity<List<TransactionDto>> getStatementByAccountNumber(@PathParam("accountNumber") Long accountNumber){

        return new ResponseEntity<>(bankOperationsSevice.transactionsOfAccount(accountNumber),HttpStatus.OK);
    }

    //returning all customers which is saved in database
    @GetMapping("/getAllCustomers")
    public ResponseEntity<List<Customer>> getAllCustomers(){
        return new ResponseEntity<List<Customer>>(customerOperationService.getAllCustomers(),HttpStatus.OK);
    }


}