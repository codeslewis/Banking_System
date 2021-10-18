package banking.repository;

import banking.entities.Account;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Optional;

public class AccountRepository {
    private static AccountRepository instance;

    private static final String dbFileName = "card.s3db";
    private static final String URL = "jdbc:sqlite:card.s3db";

    private static final String CREATE = "CREATE TABLE IF NOT EXISTS card (\n"
            + "	id integer PRIMARY KEY,\n"
            + "	number text NOT NULL UNIQUE,\n"
            + "	pin text NOT NULL,\n"
            + " balance integer\n"
            + ");";

    private final SQLiteDataSource dataSource;


    public static AccountRepository getInstance() {
        if (instance == null) {
            synchronized (AccountRepository.class) {
                if (instance == null) {
                    instance = new AccountRepository();
                }
            }
        }
        return instance;
    }

    private AccountRepository() {
        if (instance != null) {
            throw new RuntimeException("use getInstance() method to create");
        }
        this.dataSource = new SQLiteDataSource();
        this.dataSource.setUrl("jdbc:sqlite:" + dbFileName);
        createTable();
    }

    private void createTable() {
        try (Statement statement = this.dataSource.getConnection().createStatement()) {
            statement.executeUpdate(CREATE);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Account createAccount() {
        int rowsAffected = 0;
        String sql = "INSERT INTO card (number, pin, balance) VALUES (?, ?, ?)";
        Account generatedAccount = Account.generateNewAccount();
        while (rowsAffected == 0) {
            try (Connection connection = DriverManager.getConnection(URL)) {
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, generatedAccount.getCardNumber());
                    statement.setString(2, generatedAccount.getPin());
                    statement.setInt(3, generatedAccount.getBalance());
                    rowsAffected = statement.executeUpdate();
                    if (rowsAffected == 0) {
                        generatedAccount = Account.generateNewAccount();
                    }
                    System.out.println("\nYour card has been created");
                    generatedAccount.printDetails();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return generatedAccount;
    }

    public Optional<Account> readOne(String cardNumberInput, String pinInput) {
        String sql = "SELECT * FROM card WHERE number = ? AND pin = ?;";

        try (Connection connection = DriverManager.getConnection(URL)) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, cardNumberInput);
                statement.setString(2, pinInput);
                ResultSet resultSet = statement.executeQuery();

                String number = resultSet.getString("number");
                String pin = resultSet.getString("pin");
                int balance = resultSet.getInt("balance");

                return Optional.of(new Account(number, pin, balance));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

}
