package banking.ui;

import banking.entities.Account;
import banking.service.AccountService;
import banking.utils.LuhnCalculator;

import java.util.Optional;
import java.util.Scanner;

public class Menu {
    private static final Scanner stdin = new Scanner(System.in);

    private final AccountService service;

    public Menu() {
        this.service = new AccountService();
    }

    public void mainMenu() {
        MainMenu choice;
        while(true) {
            try {
                MainMenu.printMenu();
                choice = MainMenu.values()[Integer.parseInt(stdin.nextLine())];
                switch (choice) {
                    case NEW:
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

    private Optional<Account> attemptLogin() {
        System.out.println("\nEnter your card number:");
        System.out.print(">");
        String cardNumber = stdin.nextLine();
        System.out.println("Enter your PIN:");
        System.out.print(">");
        String pinNumber = stdin.nextLine();
        return service.findAccount(cardNumber, pinNumber);
    }

    private void accountMenu(Account account) {
        AccountMenu choice;
        while(true) {
            try {
                AccountMenu.printMenu();
                choice = AccountMenu.values()[Integer.parseInt(stdin.nextLine())];
                switch (choice) {
                    case BALANCE:
                        handleBalance(account);
                        break;
                    case ADD_INCOME:
                        handeAddIncome(account);
                        break;
                    case DO_TRANSFER:
                        handleDoTransfer(account);
                        break;
                    case CLOSE_ACCOUNT:
                        handleCloseAccount(account);
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

    private void handleCloseAccount(Account account) {
        service.closeAccount(account);
        System.out.println("The account has been closed!");
    }

    private void handleDoTransfer(Account account) {
        try {
            System.out.println("Transfer");
            System.out.println("Enter card number:");
            String transferTo = stdin.nextLine();
            service.validateTransferAccount(account, transferTo);
            System.out.println("Enter how much money you want to transfer:");
            int amount = Integer.parseInt(stdin.nextLine());
            service.transferRequest(account, transferTo, amount);
            System.out.println("Success!");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handeAddIncome(Account account) {
        System.out.println("Enter income:");
        int income = Integer.parseInt(stdin.nextLine());
        System.out.println(service.addIncomeToAccount(account, income) ? "Income was added!" : "ERROR");
    }

    private static void handleBalance(Account account) {
        System.out.println("Balance: " + account.getBalance());
    }

}
