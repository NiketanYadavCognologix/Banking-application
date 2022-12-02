package com.cognologix.bankingApplication.dto.responsesForBankOperations;

import com.cognologix.bankingApplication.dto.BaseResponse;

public class WithdrawAmountResponse extends BaseResponse {
    public WithdrawAmountResponse(Boolean success,String message) {
        super(success);
        this.setMessage(message);
    }
}
