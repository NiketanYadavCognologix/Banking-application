package com.cognologix.bankingApplication.dto.responsesForBankOperations;

import com.cognologix.bankingApplication.dto.BaseResponse;

public class TransferAmountResponse extends BaseResponse {
    public TransferAmountResponse(Boolean success,String message) {
        super(success);
        this.setMessage(message);
    }
}
