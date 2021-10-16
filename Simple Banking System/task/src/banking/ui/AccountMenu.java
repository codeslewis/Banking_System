package banking.ui;

import java.util.EnumSet;

public enum AccountMenu {
    EXIT("0. Exit"),
    BALANCE("1. Balance"),
    LOGOUT("2. Log out");

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
