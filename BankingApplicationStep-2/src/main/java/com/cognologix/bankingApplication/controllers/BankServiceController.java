package com.cognologix.bankingApplication.controllers;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.CreatedAccountResponse;
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
    BankOperationsService bankOperationsService;

    //creating new account by giving account DTO
    @PostMapping(value = "/createAccount",
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"})
    public ResponseEntity<?> createNewAccount(@Valid @RequestBody AccountDto accountDto) {
        CreatedAccountResponse createdAccountResponse = bankOperationsService.createAccount(accountDto);
//        System.out.println(createdAccountResponse);
//        JSONObject createdAccountJSON = new JSONObject();
//        createdAccountJSON.put("Account created Successfully....", bankOperationsService.createAccount(accountDto));
//        System.out.println(createdAccountJSON);
//        return new ResponseEntity<JSONObject>(createdAccountJSON.toMap(),HttpStatus.OK);
        return new ResponseEntity<>(createdAccountResponse,HttpStatus.OK);

    }

    //deactivating given account by account number
    @PutMapping("/deactivateAccount")
    public ResponseEntity<?> deactivateAccountByAccountNumber(@PathParam(value = "accountNumber") Long accountNumber) {
        bankOperationsService.deactivateAccountByAccountNumber(accountNumber);
        return new ResponseEntity<>("Deactivated " + accountNumber + " number account", HttpStatus.OK);
    }

    //activating given account by account number
    @PutMapping("/activateAccount")
    public ResponseEntity<?> activateAccountBYAccountNumber(@PathParam(value = "accountNumber") Long accountNumber) {
        bankOperationsService.activateAccountByAccountNumber(accountNumber);
        return new ResponseEntity<>("Activated " + accountNumber + " number account", HttpStatus.OK);
    }

    //get all deactivated accounts
    @GetMapping("/getDeactivatingAccounts")
    public ResponseEntity<List<Account>> getListOfDeactivatingAccounts() {
        return new ResponseEntity<>(bankOperationsService.getAllDeactivatedAccounts(), HttpStatus.OK);
    }

    //deposit amount to the given account number
    @PutMapping(value = "/deposit")
    public ResponseEntity<?> depositAmount(@PathParam(value = "amount") Double amount, @PathParam(value = "accountNumber") Long accountNumber) {
        String transactionMessage = bankOperationsService.deposit(accountNumber, amount);
        return new ResponseEntity<String>(transactionMessage, HttpStatus.OK);
    }

    //withdraw amount to the given account number
    @PutMapping(value = "/withdraw")
    public ResponseEntity<?> withdrawalAmount(@PathParam(value = "amount") Double amount, @PathParam(value = "accountNumber") Long accountNumber) {
        String transactionMessage = bankOperationsService.withdraw(accountNumber, amount);
        return new ResponseEntity<String>(transactionMessage, HttpStatus.OK);
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
        String transactionMessage = bankOperationsService.moneyTransfer(senderAccountNumber, receiverAccountNumber, amount);
        return new ResponseEntity<String>(transactionMessage, HttpStatus.OK);
    }


    @GetMapping("/found")
    public ResponseEntity<Account> founResponseEntity() {
        return new ResponseEntity<Account>((bankOperationsService.foundedAccount(1000111L)), HttpStatus.OK);
    }
//	public Customer updateInformation(Customer customerForUpdate);
//
//	public Customer showCustomerDetails(Integer accountId);
}
