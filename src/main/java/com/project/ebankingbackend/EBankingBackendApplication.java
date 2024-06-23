package com.project.ebankingbackend;

import com.project.ebankingbackend.dtos.BankAccountDTO;
import com.project.ebankingbackend.dtos.CurrentBankAccountDTO;
import com.project.ebankingbackend.dtos.CustomerDTO;
import com.project.ebankingbackend.dtos.SavingBankAccountDTO;
import com.project.ebankingbackend.entities.*;
import com.project.ebankingbackend.enums.AccountStatus;
import com.project.ebankingbackend.enums.OperationType;
import com.project.ebankingbackend.exceptions.CustomerNotFoundException;
import com.project.ebankingbackend.repositories.AccountOperationRepository;
import com.project.ebankingbackend.repositories.BankAccountRepository;
import com.project.ebankingbackend.repositories.CustomerRepository;
import com.project.ebankingbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EBankingBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
		return args -> {
			Stream.of("Tayeb", "Hamza", "Nasreddine", "Yassine").forEach(name -> {
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name + "@gmail.com");
				bankAccountService.saveCustomer(customer);
			});
			bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*500, 1000, customer.getId());
					bankAccountService.saveSavingBankAccount(Math.random()*5000, 6.9, customer.getId());

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
			List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
			for (BankAccountDTO bankAccount : bankAccounts) {
				for (int i = 0; i < 5; i++) {
					String accountId;
					if (bankAccount instanceof SavingBankAccountDTO) {
						accountId = ((SavingBankAccountDTO) bankAccount).getId();
					} else {
						accountId = ((CurrentBankAccountDTO) bankAccount).getId();
					}
					bankAccountService.credit(accountId, 10000+Math.random()*120000, "Credit");
					bankAccountService.debit(accountId, 1000+Math.random()*9000, "Debit");
				}
			}
		};
	}
//	@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository) {
		return args -> {
			Stream.of("Tayeb", "Nasreddine", "Yassine", "Hamza").forEach(name -> {
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name + "@gmail.com");
				customerRepository.save(customer);
			});
			customerRepository.findAll().forEach(customer -> {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()* 500);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(customer);
				currentAccount.setOverDraft(1000);
				bankAccountRepository.save(currentAccount);

				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setBalance(Math.random()* 500);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomer(customer);
				savingAccount.setInterestRate(6.9);
				bankAccountRepository.save(savingAccount);
			});
			bankAccountRepository.findAll().forEach(account -> {
				for (int i = 0; i < 5; i++) {
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random()*50);
					accountOperation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
					accountOperation.setBankAccount(account);
					accountOperationRepository.save(accountOperation);
				}
			});
		};
	}

}
