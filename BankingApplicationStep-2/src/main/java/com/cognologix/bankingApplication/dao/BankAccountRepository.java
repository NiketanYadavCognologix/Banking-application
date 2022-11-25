package com.cognologix.bankingApplication.dao;

import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.transactions.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BankAccountRepository extends JpaRepository<Account, Integer> {
    Account findByAccountNumberEquals(Long accountNumber);


}
