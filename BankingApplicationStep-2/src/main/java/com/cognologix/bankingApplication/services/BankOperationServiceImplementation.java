package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dao.TransactionRepository;
import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.CreatedAccountResponse;
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

    String transactionMessage;

    //creating and saving account into database by JPA
    @Override
    public CreatedAccountResponse createAccount(AccountDto accountDto) {
        //new account
        try {
            Account accountToSave = new Account();
            CreatedAccountResponse createdAccountResponse = new CreatedAccountResponse();

            accountToSave.setAccountID(accountDto.getAccountID());
            accountToSave.setStatus("Active");
            accountToSave.setBalance(accountDto.getBalance());
            accountToSave.setAccountNumber(generateAccountNumber());

            //check the account type is proper
            String accountTypeGivenByCustomer = accountDto.getAccountType();
            if (accountTypeGivenByCustomer.equalsIgnoreCase("Savings") || accountTypeGivenByCustomer.equalsIgnoreCase("Current")) {
                accountToSave.setAccountType(accountTypeGivenByCustomer);
            } else {
                throw new IllegalTypeOfAccountException("PLease enter valid account type...");
            }

            //adding information from AccountDTO in account
            Customer customer = customerRepository.findById(accountDto.getCustomerId()).get();
            accountToSave.setCustomer(customer);

            //check the type account for given customer is already available or not
            List<Account> matchingAccount = bankAccountRepository.findAll().stream().filter(account -> account.getCustomer().getCustomerId() == accountDto.getCustomerId()).filter(account -> account.getAccountType().equalsIgnoreCase(accountDto.getAccountType())).collect(Collectors.toList());

            //the matching account will be found then throws exception
            if (matchingAccount.isEmpty()) {
                Account account = bankAccountRepository.save(accountToSave);

                //proper response after creating new account
                createdAccountResponse.setCustomerName(account.getCustomer().getCustomerName());
                createdAccountResponse.setAccountType(account.getAccountType());
                createdAccountResponse.setAccountNumber(account.getAccountNumber());
                createdAccountResponse.setStatus(account.getStatus());
                createdAccountResponse.setBalance(account.getBalance());

                //return the custom response
                return createdAccountResponse;
            } else {
                throw new AccountAlreadyExistException("This type of account for the customer is already available...");
            }
        } catch (AccountAlreadyExistException exception) {
            exception.printStackTrace();
            throw new AccountAlreadyExistException(exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new CustomerNotFoundException("This id customer is not available...");
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
            Account account = foundedAccount(accountNumber);
            if (account == null) {
                throw new AccountNotAvailableException("Account for given Id is not available...");
            }
            return account;
        } catch (AccountNotAvailableException exception) {
            exception.printStackTrace();
            throw new AccountNotAvailableException(exception.getMessage());
        }
    }

    //deposit amount in given account number
    @Override
    public String deposit(Long accountNumber, Double amount) {
        try {
            Account accountToDeposit = foundedAccount(accountNumber);

            //checking account status
            if (accountToDeposit.getStatus().equalsIgnoreCase("deactivated")) {
                throw new DeactivateAccountException("Oop's your account id deactivated, please visit your Bank branch...");
            }
            Double updatedBalance = accountToDeposit.getBalance() + amount;
            accountToDeposit.setBalance(updatedBalance);
            bankAccountRepository.save(accountToDeposit);
            transactionMessage = amount + " deposited successfully... \nAvailable balance is : " + updatedBalance;

            //saving this transaction into transaction repository
            BankTransaction depositTransaction = new BankTransaction();

            depositTransaction.setToAccountNumber(accountNumber);
            depositTransaction.setAmount(amount);
            depositTransaction.setOperation("Deposited...");
            depositTransaction.setDateOfTransaction(LocalDateTime.now());
            transactionRepository.save(depositTransaction);
            return transactionMessage;

        } catch (DeactivateAccountException exception) {
            exception.printStackTrace();
            return "Failed to deposit, Your account is deactivated... \nPlease visit your bank branch";

//            throw new DeactivateAccountException(exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }

    //withdraw amount from given account number
    @Override
    public String withdraw(Long accountNumber, Double amount) {

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

            transactionMessage = amount + " withdraw successfully... \nAvailable balance is : " + updatedBalance;


            //saving this transaction of amount into transaction repository
            BankTransaction depositTransaction = new BankTransaction();

            depositTransaction.setFromAccountNumber(accountNumber);
            depositTransaction.setAmount(amount);
            depositTransaction.setOperation("Withdraw...");
            depositTransaction.setDateOfTransaction(LocalDateTime.now());

            //update transaction into transaction repository
            transactionRepository.save(depositTransaction);
            return transactionMessage;

        } catch (DeactivateAccountException exception) {
            exception.printStackTrace();
            return "Failed to withdraw, Your account is deactivated... \nPlease visit your bank branch";
        } catch (InsufficientBalanceException exception) {
            exception.printStackTrace();
            return "Failed to withdraw, Your account is insufficient funds...";
        }
    }

    //transfer money from one account to another account
    @Override
    public String moneyTransfer(Long accountNumberWhoSendMoney, Long accountNumberWhoReceiveMoney, Double amountForTransfer) {
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

            transactionMessage = amountForTransfer + " transferred successfully... \nAvailable balance is : " + updatedBalance;


            //saving this transaction into transaction repository
            BankTransaction depositTransaction = new BankTransaction();

            depositTransaction.setFromAccountNumber(accountNumberWhoSendMoney);
            depositTransaction.setToAccountNumber(accountNumberWhoReceiveMoney);
            depositTransaction.setAmount(amountForTransfer);
            depositTransaction.setOperation("Transferring from " + accountNumberWhoSendMoney + " to " + accountNumberWhoReceiveMoney);
            depositTransaction.setDateOfTransaction(LocalDateTime.now());
            transactionRepository.save(depositTransaction);
            return transactionMessage;
        } catch (DeactivateAccountException exception) {
            exception.printStackTrace();
            return "Failed to transfer, one of the account in transaction is deactivated... \nPlease visit your bank branch";
        } catch (InsufficientBalanceException exception) {
            exception.printStackTrace();
            return "Failed to withdraw, Your account is insufficient funds...";
        } catch (Exception exception) {
            exception.printStackTrace();
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
        } catch (AccountAlreadyDeactivatedException exception) {
            throw new AccountAlreadyDeactivatedException(exception.getMessage());
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public void activateAccountByAccountNumber(Long accountNumber) {
        try {
            Account accountToDeactivate = foundedAccount(accountNumber);
            if (accountToDeactivate.getStatus().equalsIgnoreCase("Active")) {
                throw new AccountAlreadyActivatedException("Your account is already Activated...");
            }
            accountToDeactivate.setStatus("Active");
            bankAccountRepository.save(accountToDeactivate);
        } catch (AccountAlreadyActivatedException exception) {
            exception.printStackTrace();
            throw new AccountAlreadyActivatedException(exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public List<Account> getAllDeactivatedAccounts() {
        try {
            List<Account> deactivatedAccounts = bankAccountRepository.findDeactivatedAccounts();
            if (deactivatedAccounts.isEmpty()) {
                throw new AccountNotAvailableException("No deactivated Account found...");
            }
            return deactivatedAccounts;
        } catch (AccountNotAvailableException exception) {
            exception.printStackTrace();
            throw new AccountNotAvailableException(exception.getMessage());
        }
    }

    public Account foundedAccount(Long accountNumber) {
        try {
            Account foundedAccount = bankAccountRepository.findByAccountNumberEquals(accountNumber);
            if (foundedAccount == null) {
                throw new AccountNotAvailableException("Given account numbers account is not exist...");
            }
            return foundedAccount;
        } catch (AccountNotAvailableException exception) {
            exception.printStackTrace();
            throw new AccountNotAvailableException(exception.getMessage());
        }
    }


}
