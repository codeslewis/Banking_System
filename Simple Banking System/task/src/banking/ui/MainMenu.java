package banking.ui;

import java.util.EnumSet;

public enum MainMenu {
    EXIT("0. Exit"),
    NEW("1. Create an account"),
    LOGIN("2. Log into account");

    private final String menuDefinition;

    MainMenu(String menuDefinition) {
        this.menuDefinition = menuDefinition;
    }

    public String getMenuDefinition() {
        return menuDefinition;
    }

    public static void printMenu() {
        EnumSet.allOf(MainMenu.class)
                .forEach(item -> System.out.println(item.getMenuDefinition()));
    }


}
