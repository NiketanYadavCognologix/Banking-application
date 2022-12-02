package com.cognologix.bankingApplication.dto.responsesForBankOperations;

import com.cognologix.bankingApplication.dto.BaseResponse;

public class DepositAmountResponse extends BaseResponse {
    public DepositAmountResponse(Boolean success, String message) {
        super(success);
        this.setMessage(message);
    }
}
