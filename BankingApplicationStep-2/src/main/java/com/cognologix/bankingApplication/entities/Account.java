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
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountID")
    private Integer accountID;

    @NotEmpty(message = "Please enter status")
    @Column(name = "status")
    private String status;

    @NotEmpty(message = "Please enter account type, cannot put empty Account type")
    @Column(name = "accountType")
    private String accountType;

    @NotNull(message = "Account number cannot null")
    @Column(name = "accountNumber")
    private Long accountNumber;

    @NotNull(message = "Balance cannot null")
    @Column(name = "balance")
    private Double balance;

    @OneToOne
    private Customer customer;

}
