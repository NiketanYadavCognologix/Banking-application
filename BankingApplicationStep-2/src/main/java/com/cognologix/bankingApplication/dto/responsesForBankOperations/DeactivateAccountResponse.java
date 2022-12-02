package com.cognologix.bankingApplication.dto.responsesForBankOperations;

import com.cognologix.bankingApplication.dto.BaseResponse;

public class DeactivateAccountResponse extends BaseResponse {
    public DeactivateAccountResponse(Boolean success,String message) {
        super(success);
        this.setMessage(message);
    }
}
