package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dao.TransactionRepository;
import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.entities.transactions.BankTransaction;
import com.cognologix.bankingApplication.exceptions.AccountAlreadyExistException;
import com.cognologix.bankingApplication.exceptions.InsufficientBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//service class for banking operations
@Service
public class BankOperationServiceImplementation implements BankOperationsSevice {

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
        Account accountToSave = new Account();
        accountToSave.setAccountID(accountDto.getAccountID());
        accountToSave.setAccountType(accountDto.getAccountType());
        accountToSave.setBalance(accountDto.getBalance());
        accountToSave.setAccountNumber(generateAccountNumber());

        //adding information from AccountDTO in account
        Customer customer = customerRepository.findById(accountDto.getCustomerId()).get();

        //check the type account for given customer is already available or not
        List<Account> matchingAccount = bankAccountRepository.findAll().stream()
                .filter(account -> account.getCustomer().getCustomerId() == accountDto.getCustomerId())
                .filter(account -> account.getAccountType().equalsIgnoreCase(accountDto.getAccountType()))
                .collect(Collectors.toList());

        //the matching account will be found then throws exception
        if(matchingAccount.isEmpty()) {
            accountToSave.setCustomer(customer);
            Account account = bankAccountRepository.save(accountToSave);
            return account;
        }else{
            throw new AccountAlreadyExistException("This type of account for the customer is already available...");
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
//    @Override
//    public List<Account> getAllAccountsForCustomers(Integer customerId) {
//        return dataSoucrce.getAcountsByCustomerId(customerId);
//    }

//    @Override
//    public List<Account> deleteCustomer(Integer customerId) {
//        return dataSoucrce.deleteCustomer(customerId);
//    }

    //get account by account number
    @Override
    public Account getAccountByAccountNumber(Long accountNumber) {
        return bankAccountRepository.findByAccountNumberEquals(accountNumber);
    }

    //deposit amount in given account number
    @Override
    public void deposit(Long accountNumber, Double amount) {
        Account accountToDeposit = bankAccountRepository.findByAccountNumberEquals(accountNumber);
        Double updatedBalance = accountToDeposit.getBalance() + amount;
        accountToDeposit.setBalance(updatedBalance);
        bankAccountRepository.save(accountToDeposit);

        //saving this transaction into transaction repository
        BankTransaction depositTransaction = new BankTransaction();

        depositTransaction.setToAccountNumber(accountNumber);
        depositTransaction.setAmount(amount);
        depositTransaction.setOperation("Deposited");
        depositTransaction.setDateOfTransaction(LocalDateTime.now());
        transactionRepository.save(depositTransaction);
    }

    //withdraw amount from given account number
    @Override
    public void withdraw(Long accountNumber, Double amount) {

        //JPA method by derived Query to get account by account number
        Account accountWithdraw = bankAccountRepository.findByAccountNumberEquals(accountNumber);

        //if the amount for withdraw is more than balance then throws exception
        if (accountWithdraw.getBalance() < amount) {
            throw new InsufficientBalanceException("Sorry!!! insufficient balance in your account...");
        }
        Double updatedBalance = accountWithdraw.getBalance() - amount;
        accountWithdraw.setBalance(updatedBalance);

        //updating balance into repository
        bankAccountRepository.save(accountWithdraw);

        //saving this transaction into transaction repository
        BankTransaction depositTransaction = new BankTransaction();

        depositTransaction.setFromAccountNumber(accountNumber);
        depositTransaction.setAmount(amount);
        depositTransaction.setOperation("Withdraw...");
        depositTransaction.setDateOfTransaction(LocalDateTime.now());

        //update transaction into transaction repository
        transactionRepository.save(depositTransaction);
    }

    //transfer from one account to another account
    @Override
    public void moneyTransfer(Long accountNumberWhoSendMoney, Long accountNumberWhoReceiveMoney, Double amountForTransfer) {
        //JPA method by derived Query to get account by account number
        Account accountWithdraw = bankAccountRepository.findByAccountNumberEquals(accountNumberWhoSendMoney);
        if (accountWithdraw.getBalance() < amountForTransfer) {
            throw new InsufficientBalanceException("Sorry!!! insufficient balance in your account...");
        }
        Double updatedBalance = accountWithdraw.getBalance() - amountForTransfer;
        accountWithdraw.setBalance(updatedBalance);
        bankAccountRepository.save(accountWithdraw);

        //JPA method by derived Query to get account by account number
        Account accountToDeposit = bankAccountRepository.findByAccountNumberEquals(accountNumberWhoReceiveMoney);
        Double updatedBalanceInDeposit = accountToDeposit.getBalance() + amountForTransfer;
        accountToDeposit.setBalance(updatedBalanceInDeposit);
        bankAccountRepository.save(accountToDeposit);

        //saving this transaction into transaction repository
        BankTransaction depositTransaction = new BankTransaction();

        depositTransaction.setFromAccountNumber(accountNumberWhoSendMoney);
        depositTransaction.setToAccountNumber(accountNumberWhoReceiveMoney);
        depositTransaction.setAmount(amountForTransfer);
        depositTransaction.setOperation("Transferring from " + accountNumberWhoSendMoney + " To " + accountNumberWhoReceiveMoney);
        depositTransaction.setDateOfTransaction(LocalDateTime.now());
        transactionRepository.save(depositTransaction);
    }

    //get list of transaction by account number
    //by native query fetch the transaction of particular account
    @Override
    public List<BankTransaction> transactionsOfAccount(Long AccountNumber) {

        return transactionRepository.findByToAccountNumberEquals(AccountNumber);
    }


}
