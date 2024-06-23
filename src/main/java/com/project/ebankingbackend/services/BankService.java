package com.project.ebankingbackend.services;

import com.project.ebankingbackend.entities.BankAccount;
import com.project.ebankingbackend.entities.CurrentAccount;
import com.project.ebankingbackend.entities.SavingAccount;
import com.project.ebankingbackend.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter(){
        BankAccount bankAccount=
                bankAccountRepository.findById("0b36be78-8d5d-446b-9f20-37eadc9d3c3b").orElse(null);
        if(bankAccount!=null) {
            System.out.println("*****************************");
            System.out.println(bankAccount.getId());
            System.out.println(bankAccount.getBalance());
            System.out.println(bankAccount.getStatus());
            System.out.println(bankAccount.getCreatedAt());
            System.out.println(bankAccount.getCustomer().getName());
            System.out.println(bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount) {
                System.out.println("Overdraft: " + ((CurrentAccount) bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("Interest rate: " + ((SavingAccount) bankAccount).getInterestRate());
            }
            bankAccount.getAccountOperations().forEach(accountOperation -> {
                System.out.println(accountOperation.getType() + "\t" + accountOperation.getOperationDate() + "\t" + accountOperation.getAmount());
            });
        }
    }
}