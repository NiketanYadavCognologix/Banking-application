package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.CreatedAccountResponse;
import com.cognologix.bankingApplication.dto.TransactionDto;
import com.cognologix.bankingApplication.entities.Account;

import java.util.List;

public interface BankOperationsService {

    CreatedAccountResponse createAccount(AccountDto accountDto);

    Account getAccountByAccountNumber(Long accountId);

    String deposit(Long accountNumber, Double ammount);

    String withdraw(Long accountNumber, Double ammount);

    String moneyTransfer(Long accountNumberWhoSendMoney,Long accountNumberWhoRecieveMoney,Double ammountForTransfer);

    List<TransactionDto> transactionsOfAccount(Long fromAccountNumber);

    void deactivateAccountByAccountNumber(Long accountNumber);

    void activateAccountByAccountNumber(Long accountNumber);

    List<Account> getAllDeactivatedAccounts();
    Account foundedAccount(Long accountNumber);
}
