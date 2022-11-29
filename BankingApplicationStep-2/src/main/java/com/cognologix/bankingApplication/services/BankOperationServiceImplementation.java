package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dao.TransactionRepository;
import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.TransactionDto;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.entities.transactions.BankTransaction;
import com.cognologix.bankingApplication.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//service class for banking operations
@Service
public class BankOperationServiceImplementation implements BankOperationsService {

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TransactionRepository transactionRepository;

    //creating and saving account into database by JPA
    @Override
    public Account createAccount(AccountDto accountDto) {
        //new account
        try {
            Account accountToSave = new Account();
            accountToSave.setAccountID(accountDto.getAccountID());
            accountToSave.setAccountType(accountDto.getAccountType());
            accountToSave.setStatus("Active");
            accountToSave.setBalance(accountDto.getBalance());
            accountToSave.setAccountNumber(generateAccountNumber());

            //adding information from AccountDTO in account
            Customer customer = customerRepository.findById(accountDto.getCustomerId()).get();

            //check the type account for given customer is already available or not
            List<Account> matchingAccount = bankAccountRepository.findAll().stream().filter(account -> account.getCustomer().getCustomerId() == accountDto.getCustomerId()).filter(account -> account.getAccountType().equalsIgnoreCase(accountDto.getAccountType())).collect(Collectors.toList());

            //the matching account will be found then throws exception
            if (matchingAccount.isEmpty()) {
                accountToSave.setCustomer(customer);
                Account account = bankAccountRepository.save(accountToSave);
                return account;
            } else {
                throw new AccountAlreadyExistException("This type of account for the customer is already available...");
            }
        } catch (AccountAlreadyExistException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    //auto generating account number for every new account
    public Long generateAccountNumber() {
//        Random random = new Random();
//        String accountNumberInString = String.valueOf(Math.round(random.nextFloat() * Math.pow(10, 12)));
        Long accountNumber = Long.valueOf(1000 + bankAccountRepository.findAll().size());
//        Long accountNumber = Long.parseLong(accountNumberInString);
        return accountNumber;
    }

    //get account by account number
    @Override
    public Account getAccountByAccountNumber(Long accountNumber) {
        try {
            Account account = bankAccountRepository.findByAccountNumberEquals(accountNumber);
            if (account == null) {
                throw new AccountNotFoundException("Account for given Id is not available...");
            }
            return account;
        } catch (AccountNotFoundException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    //deposit amount in given account number
    @Override
    public void deposit(Long accountNumber, Double amount) {
        try {
            Account accountToDeposit = bankAccountRepository.findByAccountNumberEquals(accountNumber);

            //checking account status
            if (accountToDeposit.getStatus().equalsIgnoreCase("deactivated")) {
                throw new DeactivateAccountException("Oop's your account id deactivated, please visit your Bank branch...");
            }
            Double updatedBalance = accountToDeposit.getBalance() + amount;
            accountToDeposit.setBalance(updatedBalance);
            bankAccountRepository.save(accountToDeposit);

            //saving this transaction into transaction repository
            BankTransaction depositTransaction = new BankTransaction();

            depositTransaction.setToAccountNumber(accountNumber);
            depositTransaction.setAmount(amount);
            depositTransaction.setOperation("Deposited...");
            depositTransaction.setDateOfTransaction(LocalDateTime.now());
            transactionRepository.save(depositTransaction);
        } catch (DeactivateAccountException exception) {
            throw new RuntimeException(exception.getMessage());
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    //withdraw amount from given account number
    @Override
    public void withdraw(Long accountNumber, Double amount) {

        try {
            //JPA method by derived Query to get account by account number
            Account accountWithdraw = foundedAccount(accountNumber);

            //checking account status
            if (accountWithdraw.getStatus().equalsIgnoreCase("deactivated")) {
                throw new DeactivateAccountException("Oop's your account id deactivated, please visit your Bank branch...");
            }

            //if the amount for withdraw is more than balance then throws exception
            if (accountWithdraw.getBalance() < amount) {
                throw new InsufficientBalanceException("Sorry!!! insufficient balance in your account...");
            }
            Double updatedBalance = accountWithdraw.getBalance() - amount;
            accountWithdraw.setBalance(updatedBalance);

            //updating balance into repository
            bankAccountRepository.save(accountWithdraw);

            //saving this transaction of amount into transaction repository
            BankTransaction depositTransaction = new BankTransaction();

            depositTransaction.setFromAccountNumber(accountNumber);
            depositTransaction.setAmount(amount);
            depositTransaction.setOperation("Withdraw...");
            depositTransaction.setDateOfTransaction(LocalDateTime.now());

            //update transaction into transaction repository
            transactionRepository.save(depositTransaction);
        } catch (DeactivateAccountException exception) {
            throw new RuntimeException(exception.getMessage());
        } catch (InsufficientBalanceException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    //transfer money from one account to another account
    @Override
    public void moneyTransfer(Long accountNumberWhoSendMoney, Long accountNumberWhoReceiveMoney, Double amountForTransfer) {
        try {
            //JPA method by derived Query to get account by account number
            Account accountWithdraw = foundedAccount(accountNumberWhoSendMoney);

            if (accountWithdraw.getStatus().equalsIgnoreCase("deactivated")) {
                throw new DeactivateAccountException("Oop's your account id deactivated, please visit your Bank branch...");
            }
            if (accountWithdraw.getBalance() < amountForTransfer) {
                throw new InsufficientBalanceException("Sorry!!! insufficient balance in your account...");
            }
            Double updatedBalance = accountWithdraw.getBalance() - amountForTransfer;
            accountWithdraw.setBalance(updatedBalance);
            bankAccountRepository.save(accountWithdraw);

            //JPA method by derived Query to get account by account number
            Account accountToDeposit = foundedAccount(accountNumberWhoReceiveMoney);

            //checking account status
            if (accountToDeposit.getStatus().equalsIgnoreCase("deactivated")) {
                throw new DeactivateAccountException("Oop's your account id deactivated, please visit your Bank branch...");
            }

            Double updatedBalanceInDeposit = accountToDeposit.getBalance() + amountForTransfer;
            accountToDeposit.setBalance(updatedBalanceInDeposit);
            bankAccountRepository.save(accountToDeposit);

            //saving this transaction into transaction repository
            BankTransaction depositTransaction = new BankTransaction();

            depositTransaction.setFromAccountNumber(accountNumberWhoSendMoney);
            depositTransaction.setToAccountNumber(accountNumberWhoReceiveMoney);
            depositTransaction.setAmount(amountForTransfer);
            depositTransaction.setOperation("Transferring from " + accountNumberWhoSendMoney + " to " + accountNumberWhoReceiveMoney);
            depositTransaction.setDateOfTransaction(LocalDateTime.now());
            transactionRepository.save(depositTransaction);
        } catch (DeactivateAccountException exception) {
            throw new RuntimeException(exception.getMessage());
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    //get list of transaction by account number
    //by native query fetch the transaction of particular account
    @Override
    public List<TransactionDto> transactionsOfAccount(Long AccountNumber) {

        List<TransactionDto> transactionDtos = new ArrayList<>();

        //set values in transactionDto to get proper formatted output of transaction
        transactionRepository.findByToAccountNumberEquals(AccountNumber).stream().forEach(transaction -> {
            TransactionDto transactionDto = new TransactionDto();

            transactionDto.setTransactionId(transaction.getTransactionId());
            transactionDto.setOperation(transaction.getOperation());
            transactionDto.setAmount(transaction.getAmount());
            transactionDto.setDateOfTransaction(transaction.getDateOfTransaction());

            transactionDtos.add(transactionDto);
        });

        return transactionDtos;
    }

    @Override
    public void deactivateAccountByAccountNumber(Long accountNumber) {
        try {
            Account accountToDeactivate = foundedAccount(accountNumber);
            if (accountToDeactivate.getStatus().equalsIgnoreCase("deactivated")) {
                throw new AccountAlreadyDeactivatedException("Your account is already deactivated...");
            }
            accountToDeactivate.setStatus("deactivated");
            bankAccountRepository.save(accountToDeactivate);
        }catch (AccountAlreadyDeactivatedException exception){
            throw new RuntimeException(exception.getMessage());
        }catch (Exception exception){
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public void activateAccountByAccountNumber(Long accountNumber) {
        try {
            Account accountToDeactivate = foundedAccount(accountNumber);
            if (accountToDeactivate.getStatus().equalsIgnoreCase("activate")) {
                throw new AccountAlreadyActivatedException("Your account is already Activated...");
            }
            accountToDeactivate.setStatus("activate");
            bankAccountRepository.save(accountToDeactivate);
        }catch (AccountAlreadyActivatedException exception){
            throw new RuntimeException(exception.getMessage());
        }catch (Exception exception){
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public List<Account> getAllDeactivatedAccounts() {
       List<Account> deactivatedAccounts = bankAccountRepository.findDeactivatedAccounts();
       if(deactivatedAccounts.isEmpty()){
            throw new AccountNotAvailableException("No deactivated Account found...");
       }
        return deactivatedAccounts;
    }

    public Account foundedAccount(Long accountNumber){
        Account foundedAccount= bankAccountRepository.findByAccountNumberEquals(accountNumber);
        if(foundedAccount==null){
            throw new AccountNotAvailableException("Given account numbers account is not exist...");
        }
        return foundedAccount;
    }


}
