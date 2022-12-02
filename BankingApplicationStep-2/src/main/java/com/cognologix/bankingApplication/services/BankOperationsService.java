package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.responsesForBankOperations.ActivateAccountResponse;
import com.cognologix.bankingApplication.dto.responsesForBankOperations.CreatedAccountResponse;
import com.cognologix.bankingApplication.dto.responsesForBankOperations.DeactivateAccountResponse;
import com.cognologix.bankingApplication.dto.responsesForBankOperations.DeactivatedAccountsResponse;
import com.cognologix.bankingApplication.dto.responsesForBankOperations.DepositAmountResponse;
import com.cognologix.bankingApplication.dto.responsesForBankOperations.TransferAmountResponse;
import com.cognologix.bankingApplication.dto.responsesForBankOperations.WithdrawAmountResponse;
import com.cognologix.bankingApplication.dto.responsesForCustomerOperations.TransactionStatementResponse;
import com.cognologix.bankingApplication.entities.Account;

public interface BankOperationsService {

    CreatedAccountResponse createAccount(AccountDto accountDto);

    Account getAccountByAccountNumber(Long accountId);

    DepositAmountResponse deposit(Long accountNumber, Double amount);

    WithdrawAmountResponse withdraw(Long accountNumber, Double amount);

    TransferAmountResponse moneyTransfer(Long accountNumberWhoSendMoney,Long accountNumberWhoReceiveMoney,Double amountForTransfer);

    TransactionStatementResponse transactionsOfAccount(Long fromAccountNumber);

    DeactivateAccountResponse deactivateAccountByAccountNumber(Long accountNumber);

    ActivateAccountResponse activateAccountByAccountNumber(Long accountNumber);

    DeactivatedAccountsResponse getAllDeactivatedAccounts();
}
