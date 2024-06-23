package com.project.ebankingbackend.dtos;

import com.project.ebankingbackend.entities.BankAccount;
import com.project.ebankingbackend.enums.OperationType;
import lombok.Data;

import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}
