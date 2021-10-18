package banking.ui;

import java.util.EnumSet;

public enum AccountMenu {
    EXIT("0. Exit"),
    BALANCE("1. Balance"),
    ADD_INCOME("2. Add income"),
    DO_TRANSFER("3. Do transfer"),
    CLOSE_ACCOUNT("4. Close account"),
    LOGOUT("5. Log out");

    private final String menuDefinition;

    AccountMenu(String menuDefinition) {
        this.menuDefinition = menuDefinition;
    }

    public String getMenuDefinition() {
        return menuDefinition;
    }

    public static void printMenu() {
        EnumSet.allOf(AccountMenu.class)
                .forEach(item -> System.out.println(item.getMenuDefinition()));
    }
}
