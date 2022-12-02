package com.cognologix.bankingApplication.dto.responsesForCustomerOperations;

import com.cognologix.bankingApplication.dto.BaseResponse;

public class CustomerUpdateResponse extends BaseResponse{

    public CustomerUpdateResponse(Boolean success,String message) {
        super(success);
        this.setMessage(message);
    }
}