package com.cognologix.bankingApplication.dao;

import com.cognologix.bankingApplication.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cognologix.bankingApplication.entities.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer>{
    Account findByCustomerIdEquals(Integer customerId);

}
