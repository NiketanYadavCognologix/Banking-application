package com.cognologix.bankingApplication.dto;

import com.cognologix.bankingApplication.entities.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class CreatedAccountResponse {

    private String customerName;

    private String accountType;

    private Long accountNumber;

    private String status;

    private Double balance;

}
