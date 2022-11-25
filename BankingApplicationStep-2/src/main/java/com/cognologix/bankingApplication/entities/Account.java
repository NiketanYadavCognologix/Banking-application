package com.cognologix.bankingApplication.entities;

import com.cognologix.bankingApplication.entities.transactions.BankTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@XmlRootElement
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountID;

    @NotEmpty(message = "Type of account cannot null")
    private String accountType;

    @NotNull(message = "Account number cannot blank")
    private Long accountNumber;

    @NotNull(message = "Balance cannot null")
    private Double balance;

    @OneToOne
    private Customer customer;

}
