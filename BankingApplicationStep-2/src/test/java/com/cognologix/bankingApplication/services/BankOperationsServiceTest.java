package com.cognologix.bankingApplication.services;

import com.cognologix.bankingApplication.dao.BankAccountRepository;
import com.cognologix.bankingApplication.dao.CustomerRepository;
import com.cognologix.bankingApplication.dto.AccountDto;
import com.cognologix.bankingApplication.dto.responsesForBankOperations.CreatedAccountResponse;
import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class BankOperationsServiceTest {


    @Mock
    BankAccountRepository bankAccountRepository;

    @Mock
    CustomerRepository customerRepository;
    @Mock
    private BankOperationsService bankOperationsSevice;

    @Mock
    private CustomerOperationService customerOperationService;


    Customer customer = new Customer(1, "Poorv", LocalDate.of(1998, 10, 23),
            "123456799012", "1Poo456789", "niketanya33dav@gmail.com", "Female");
//    CustomerDto customerDto = new CustomerDto(1, "Niketan", LocalDate.of(1998, 10, 23),
//            "123456789012", "123456789", "niketanyadav@gmail.com", "Male");
//
//    Account accountInstance = new Account(2, "saavings", 1001L, 1000.00, customer);
    AccountDto accountDto = new AccountDto(1,
            "savings", 1000.0, 1);
    Account account = new Account(accountDto.getAccountID(),"Activate", accountDto.getAccountType(),
            1000L, accountDto.getBalance(), customer);

//    Account account=new Account()
//    AccountDto accountDto2 = new AccountDto(accountInstance.getAccountID(),
//            accountInstance.getAccountType(), accountInstance.getBalance(), accountInstance.getCustomer().getCustomerId());

    Customer customer1;
    CreatedAccountResponse actualCreatedREsponse;

//    @Before
//    public void init() {
//       // MockitoAnnotations.openMocks(this);
//        Account account = new Account(accountDto.getAccountID(),"Activate", accountDto.getAccountType(),
//                1000L, accountDto.getBalance(), customer);
//    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

//    @BeforeEach
//    public void setUp() {
//        customer1 = customerOperationService.createNewCustomer(customer);
//        actualCreatedREsponse = bankOperationsSevice.createAccount(accountDto);
//        System.out.println(actualCreatedREsponse);
//    }

    @Test
    void createAccount() {
//        setUp();
        System.out.println("**********1****************");
//       Customer customer2= customerOperationService.createNewCustomer(customer);
//        System.out.println(customer2+"-------------->1");
        System.out.println("**********2****************");
        actualCreatedREsponse = bankOperationsSevice.createAccount(accountDto);
        System.out.println("**********3****************");
        System.out.println(actualCreatedREsponse);
//        when(bankOperationsSevice.createAccount(accountDto)).thenReturn(account);
//        assertEquals(account,bankOperationsSevice.);
    }

    @Test
    void getAccountByAccountNumber() {
//        setUp();
        when(bankOperationsSevice.getAccountByAccountNumber(1000L)).thenReturn(account);
        assertEquals(bankOperationsSevice.getAccountByAccountNumber(1000L), account);
    }

    @Test
    void deposit() {
//        setUp();
       Account account2 = bankOperationsSevice.getAccountByAccountNumber(1000L);
        System.out.println(account2);
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