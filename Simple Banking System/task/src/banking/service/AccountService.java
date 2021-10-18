package banking.service;

import banking.entities.Account;
import banking.repository.AccountRepository;
import banking.utils.LuhnCalculator;

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

    public boolean addIncomeToAccount(Account account, int income) {
        return repository.addIncome(account, income);
    }

    public boolean transferRequest(Account account, String transferee, int amount) {
        hasSufficientBalance(account.getCardNumber(), amount);
        repository.transferMoney(account, transferee, amount);
        return true;
    }

    private void hasSufficientBalance(String cardNumber, int amountRequired) {
        int balance = repository.getBalanceForAccount(cardNumber);
        if (balance < amountRequired) {
            throw new RuntimeException("Not enough money!");
        }
    }

    public void validateTransferAccount(Account origin, String transferee) {
        if (origin.getCardNumber().equals(transferee)) {
            throw new RuntimeException("You can't transfer money to the same account!");
        }
        if (!LuhnCalculator.validateCheckSum(transferee)) {
            throw new RuntimeException("Probably you made a mistake in the card number. Please try again!");
        }
        if (!repository.accountExists(transferee)) {
            throw new RuntimeException("Such a card does not exist.");
        }
    }

    public void closeAccount(Account account) {
        repository.deleteOne(account.getCardNumber());
    }
}
