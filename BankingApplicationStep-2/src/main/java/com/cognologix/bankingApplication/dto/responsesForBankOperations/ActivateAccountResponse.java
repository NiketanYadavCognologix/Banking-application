package com.cognologix.bankingApplication.dto.responsesForBankOperations;

import com.cognologix.bankingApplication.dto.BaseResponse;

public class ActivateAccountResponse extends BaseResponse {
    public ActivateAccountResponse(Boolean success,String message) {
        super(success);
        this.setMessage(message);
    }
}
