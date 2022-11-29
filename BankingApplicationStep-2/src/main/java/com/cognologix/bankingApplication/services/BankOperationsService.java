package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.TransactionDto;
import com.cognologix.bankingApplication.entities.Account;

import java.util.List;

public interface BankOperationsService {

    Account createAccount(AccountDto accountDto);

    Account getAccountByAccountNumber(Long accountId);

    void deposit(Long accountNumber, Double ammount);

    void withdraw(Long accountNumber, Double ammount);

    void moneyTransfer(Long accountNumberWhoSendMoney,Long accountNumberWhoRecieveMoney,Double ammountForTransfer);

    List<TransactionDto> transactionsOfAccount(Long fromAccountNumber);

    void deactivateAccountByAccountNumber(Long accountNumber);

    void activateAccountByAccountNumber(Long accountNumber);

    List<Account> getAllDeactivatedAccounts();
}
