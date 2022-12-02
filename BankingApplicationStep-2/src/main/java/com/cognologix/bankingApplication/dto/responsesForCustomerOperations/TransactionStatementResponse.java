package com.cognologix.bankingApplication.dto.responsesForCustomerOperations;

import com.cognologix.bankingApplication.dto.BaseResponse;
import com.cognologix.bankingApplication.dto.TransactionDto;
import com.cognologix.bankingApplication.entities.transactions.BankTransaction;

import java.util.List;

public class TransactionStatementResponse extends BaseResponse {
    List<TransactionDto> bankTransactions;

    public TransactionStatementResponse(Boolean success, List<TransactionDto> bankTransactions) {
        super(success);
        this.bankTransactions = bankTransactions;
    }
}
