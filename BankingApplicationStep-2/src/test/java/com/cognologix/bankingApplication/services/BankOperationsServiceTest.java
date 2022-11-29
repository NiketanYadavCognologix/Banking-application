package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.CustomerDto;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import com.cognologix.bankingApplication.globleObjectLists.DataSoucrce;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class)
class BankOperationsServiceTest {


    @Mock
    private BankOperationsService bankOperationsSevice;

    @Mock
    private CustomerOperationService customerOperationService;

    @Mock
    private DataSoucrce dataSoucrce;

    Customer customer = new Customer(1, "Niketan", LocalDate.of(1998, 10, 23),
            "123456789012", "123456789", "niketanyadav@gmail.com", "Male");
//    CustomerDto customerDto = new CustomerDto(1, "Niketan", LocalDate.of(1998, 10, 23),
//            "123456789012", "123456789", "niketanyadav@gmail.com", "Male");
//
//    Account accountInstance = new Account(2, "saavings", 1001L, 1000.00, customer);
    AccountDto accountDto = new AccountDto(1,
            "savings", 1000.0, 1);
    Account account = new Account(accountDto.getAccountID(), accountDto.getAccountType(),
            1000L, accountDto.getBalance(), customer);

//    AccountDto accountDto2 = new AccountDto(accountInstance.getAccountID(),
//            accountInstance.getAccountType(), accountInstance.getBalance(), accountInstance.getCustomer().getCustomerId());

    Customer customer1;
    Account account1;

//    @Before
//    public void init() {
//        MockitoAnnotations.openMocks(this);
//    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Before
    public void setUp() {
//        customer1 = customerOperationService.createNewCustomer(customer);
//        account1 = bankOperationsSevice.createAccount(accountDto);
    }

    @Test
    void createAccount() {
//        setUp();
        when(bankOperationsSevice.createAccount(accountDto)).thenReturn(account);
        assertEquals((bankOperationsSevice.createAccount(accountDto)), account);
    }

    @Test
    void getAccountByAccountNumber() {
//        setUp();
        when(bankOperationsSevice.getAccountByAccountNumber(1000L)).thenReturn(account);
        assertEquals(bankOperationsSevice.getAccountByAccountNumber(1000L), account);
    }

    @Test
    void deposit() {
        setUp();
       Account account2 = bankOperationsSevice.getAccountByAccountNumber(1000L);
        System.out.println(account1);
//        Double expectedBalance = account.getBalance()+300;
//        Account account2=bankOperationsSevice.getAccountByAccountNumber(1000L);
//        bankOperationsSevice.deposit(account.getAccountNumber(),200.0);
//        System.out.println(bankOperationsSevice.getAccountByAccountNumber(1000L).getBalance()+"balance");
//        assertEquals(expectedBalance,bankOperationsSevice.getAccountByAccountNumber(1000L).getBalance());
    }

    @Test
    void withdraw() {
        setup();

    }

    @Test
    void moneyTransfer() {
    }

    @Test
    void transactionsOfAccount() {
    }
}