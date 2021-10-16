package banking.service;

import banking.entities.Account;
import banking.repository.AccountRepository;

import java.util.Optional;

public class AccountService {

    private final AccountRepository repository;

    public AccountService() {
        this.repository = AccountRepository.getInstance();
    }

    public Account newAccount() {
        return repository.createAccount();
    }

    public Optional<Account> findAccount(String cardNumber, String pinNumber) {
        return repository.readOne(cardNumber, pinNumber);
    }
}
