package com.cognologix.bankingApplication.dto.responsesForCustomerOperations;

import com.cognologix.bankingApplication.dto.BaseResponse;

public class BalanceInquiryResponse extends BaseResponse{
    public BalanceInquiryResponse(Boolean success,String message) {
        super(success);
        this.setMessage(message);
    }
}
