package com.cognologix.bankingApplication.dto.responsesForBankOperations;

import com.cognologix.bankingApplication.dto.BaseResponse;
import com.cognologix.bankingApplication.entities.Account;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedAccountResponse extends BaseResponse {
    private String customerName;
    private String accountType;
    private Long accountNumber;
    private String status;
    private Double balance;

    public CreatedAccountResponse(Boolean success,String message,Account account) {
        super(success);
        this.setMessage(message);
        this.customerName = account.getCustomer().getCustomerName();
        this.accountType = account.getAccountType();
        this.accountNumber = account.getAccountNumber();
        this.status = account.getStatus();
        this.balance = account.getBalance();
    }
}
