package com.cognologix.bankingApplication.controllers;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.entities.Account;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cognologix.bankingApplication.services.BankOperationsService;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/banking")
public class BankServiceController {

    //bank side operations
    @Autowired
    BankOperationsService bankOperationsSevice;
    JSONObject resultSet;

    //creating new account by givng account DTO
    @PostMapping("/createAccount")
    public ResponseEntity<Account> createNewAccount(@Valid @RequestBody AccountDto accountDto) {
        Account createdAccount = bankOperationsSevice.createAccount(accountDto);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);

    }

    //deactivating given account by account number
    @PutMapping("/deactivateAccount")
    public ResponseEntity<?> deactivateAccountByAccountNumber(@PathParam(value = "accountNumber") Long accountNumber) {
        bankOperationsSevice.deactivateAccountByAccountNumber(accountNumber);
        return new ResponseEntity<>("Deactivated " + accountNumber + " number account", HttpStatus.OK);
    }

    //activating given account by account number
    @PutMapping("/activateAccount")
    public ResponseEntity<?> activateAccountBYAccountNumber(@PathParam(value = "accountNumber") Long accountNumber) {
        bankOperationsSevice.activateAccountByAccountNumber(accountNumber);
        return new ResponseEntity<>("Activated " + accountNumber + " number account", HttpStatus.OK);
    }

    //get all deactivated accounts
    @GetMapping("/getDeactivatingAccounts")
    public ResponseEntity<List<Account>> getListOfDeactivatingAccounts() {
        return new ResponseEntity<>(bankOperationsSevice.getAllDeactivatedAccounts(),HttpStatus.OK);
    }

    //deposit amount to the given account number
    @PutMapping(value = "/deposit")
    public ResponseEntity<?> depositAmount(@PathParam(value = "amount") Double amount, @PathParam(value = "accountNumber") Long accountNumber) {
        bankOperationsSevice.deposit(accountNumber, amount);
        return new ResponseEntity<String>("Amount deposit successfully...", HttpStatus.OK);
    }

    //withdraw amount to the given account number
    @PutMapping(value = "/withdraw")
    public ResponseEntity<?> withdrawalAmount(@PathParam(value = "amount") Double amount, @PathParam(value = "accountNumber") Long accountNumber) {
        bankOperationsSevice.withdraw(accountNumber, amount);
        return new ResponseEntity<String>("Amount withdraw successfully...", HttpStatus.OK);
    }

    //return accounts list of single customer by id
//    @GetMapping("/getAllByCustomerID/{customerID}")
//    public ResponseEntity<?> getAllAccounts(@PathVariable Integer customerID) {
//        List<Account> accounts = bankOperationsSevice.getAllAccountsForCustomers(customerID);
//        return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
//    }


    //transferring amount from one account to another account
    @PutMapping("/transfer")
    public ResponseEntity<?> moneyTransfer(@PathParam(value = "senderAccountNumber") Long senderAccountNumber,
                                           @PathParam(value = "receiverAccountNumber") Long receiverAccountNumber,
                                           @PathParam(value = "amount") Double amount) {
        bankOperationsSevice.moneyTransfer(senderAccountNumber, receiverAccountNumber, amount);
        return new ResponseEntity<String>("Amount successfully transfer...", HttpStatus.OK);
    }


//	public Customer updateInformation(Customer customerForUpdate);
//
//	public Customer showCustomerDetails(Integer accountId);
}
