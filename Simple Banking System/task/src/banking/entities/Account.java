package banking.entities;

import banking.utils.LuhnCalculator;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class Account {
    private static final String INN = "400000";
    private String cardNumber;
    private String pin;

    private int balance;

    public Account(String cardNumber, String pin, int balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    private Account() {}

    public static Account generateNewAccount() {
        Account result = new Account();
        result.cardNumber = generateCardNumber();
        result.pin = generatePin();
        return result;
    }

    private static String generatePin() {
        return new Random()
                .ints(4, 0, 9)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

    private static String generateCardNumber() {
        int[] digits = new Random()
                .ints(9,0,9)
                .toArray();
        int checkDigit = LuhnCalculator.getCheckSum(INN, digits);
        return INN + Arrays.stream(digits)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining()) +
                checkDigit;
    }


    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }

    public void printDetails() {
        System.out.println("Your card number:");
        System.out.println(getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(getPin());
        System.out.println();
    }

}
