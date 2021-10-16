package banking.repository;

import banking.entities.Account;
import org.sqlite.SQLiteDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class AccountRepository {
    private static AccountRepository instance;

    private static final String dbFileName = "card.s3db";

    private static final String CREATE = "CREATE TABLE IF NOT EXISTS card (\n"
            + "	id integer PRIMARY KEY,\n"
            + "	number text NOT NULL,\n"
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
        Account generatedAccount = Account.generateNewAccount();
        String sql = "INSERT INTO card(number, pin, balance) VALUES " +
                String.format("('%s', '%s', %d)",
                generatedAccount.getCardNumber(),
                generatedAccount.getPin(),
                generatedAccount.getBalance());

        try (Statement statement = this.dataSource.getConnection().createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("\nYour card has been created");
            generatedAccount.printDetails();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedAccount;
    }

    public Optional<Account> readOne(String cardNumberInput, String pinInput) {
        String sql = "SELECT * FROM card WHERE " +
                String.format("number = %s AND pin = %s", cardNumberInput, pinInput);

        try (Statement statement = this.dataSource.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            String number = resultSet.getString("number");
            String pin = resultSet.getString("pin");
            int balance = resultSet.getInt("balance");

            return Optional.of(new Account(number, pin, balance));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
