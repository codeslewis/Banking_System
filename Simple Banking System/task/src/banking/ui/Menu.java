package banking.ui;

import banking.entities.Account;
import banking.repository.AccountRepository;
import banking.service.AccountService;

import java.util.Optional;
import java.util.Scanner;

public class Menu {
    private static final Scanner stdin = new Scanner(System.in);

    private final AccountRepository repository;
    private final AccountService service;

    public Menu() {
        this.service = new AccountService();
        this.repository = AccountRepository.getInstance();
    }

    public void mainMenu() {
        MainMenu choice;
        while(true) {
            try {
                MainMenu.printMenu();
                choice = MainMenu.values()[Integer.parseInt(stdin.nextLine())];
                switch (choice) {
                    case NEW:
//                        repository.createAccount();
                        service.newAccount();
                        break;
                    case LOGIN:
                        Optional<Account> current = attemptLogin();
                        if (current.isPresent()) {
                            System.out.println("\nYou have successfully logged in!\n");
                            accountMenu(current.get());
                        } else {
                            System.out.println("\nWrong card number or PIN!\n");
                        }
                        break;
                    case EXIT:
                        System.out.println("Bye!");
                        return;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Inappropriate input");
            }
        }
    }

    public void accountMenu(Account account) {
        AccountMenu choice;
        while(true) {
            try {
                AccountMenu.printMenu();
                choice = AccountMenu.values()[Integer.parseInt(stdin.nextLine())];
                switch (choice) {
                    case BALANCE:
                        System.out.println("Balance: " + account.getBalance());;
                        break;
                    case LOGOUT:
                        System.out.println("You have successfully logged out");
                        return;
                    case EXIT:
                        System.out.println("Bye!");
                        System.exit(0);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Inappropriate input");
            }
        }
    }

    private Optional<Account> attemptLogin() {
        System.out.println("\nEnter your card number:");
        System.out.print(">");
        String cardNumber = stdin.nextLine();
        System.out.println("Enter your PIN:");
        System.out.print(">");
        String pinNumber = stdin.nextLine();
        return repository.readOne(cardNumber, pinNumber);
//        return service.findAccount(cardNumber, pinNumber);
    }
}
