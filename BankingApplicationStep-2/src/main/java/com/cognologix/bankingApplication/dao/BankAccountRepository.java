package com.cognologix.bankingApplication.dao;

import com.cognologix.bankingApplication.entities.Account;
import com.cognologix.bankingApplication.entities.transactions.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<Account, Integer> {
    Account findByAccountNumberEquals(Long accountNumber);
//    String status="Activate";
//    List<Account> findByStatusEquals(String status);

    @Query(value = "SELECT * FROM account WHERE status='Deactivated' OR status='deactivated'", nativeQuery = true)
    List<Account> findDeactivatedAccounts();


}
