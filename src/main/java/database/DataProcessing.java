package database;

import UI.PaymentTab;
import org.h2.tools.RunScript;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataProcessing {
    private static Connection connection;

    public static void connectToDatabase() throws SQLException {
        try {
            Class.forName("org.h2.Driver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:h2:~/accounting/data;ifexists=true", "", "");
            System.out.println("Database connection successful");
        } catch (SQLException e) {
            connection = DriverManager.getConnection("jdbc:h2:~/accounting/data;create=true", "", "");
            initDatabase();
            System.out.println("Created new database");
        }
    }

    private static void initDatabase() throws SQLException {
        Path path = Paths.get("init.sql");
        BufferedReader reader1 = null;
        try {
            reader1 = Files.newBufferedReader(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            RunScript.execute(connection, reader1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTableBasedOnLocalDate(LocalDate date) throws SQLException {
        Statement statement;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM_yyyy", new Locale("en"));
        String query = "CREATE TABLE IF NOT EXISTS " + formatter.format(date);
        statement = connection.createStatement();
        statement.execute(query);

    }

    public static int insertPaymentIntoDatabase(PaymentTab.Payment payment) {
        try {
            PreparedStatement preparedInsertStatement;

            String insertStatement = "INSERT INTO ACCOUNTING.PAYMENTS(DATE, NUMBER, PAYMENT, TYPE, AMOUNT) VALUES" + "(?,?,?,?,?)";

            preparedInsertStatement = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate;
            formattedDate = formatter.format(payment.getDate());
            connection.setAutoCommit(false);
            preparedInsertStatement.setDate(1, Date.valueOf(formattedDate));
            preparedInsertStatement.setInt(2, payment.getNumber());
            preparedInsertStatement.setString(3, payment.getPayment());
            preparedInsertStatement.setString(4, payment.getType());
            preparedInsertStatement.setInt(5, payment.getSum());

            preparedInsertStatement.executeUpdate();

            ResultSet resultSet = preparedInsertStatement.getGeneratedKeys();
            int key = 0;
            while (resultSet.next()) {
                key = resultSet.getInt(1);
            }
            connection.commit();
            connection.setAutoCommit(true);
            return key;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static void updateValueInDatabase(PaymentTab.Payment payment) {
        PreparedStatement preparedUpdateStatement;

        try {
            preparedUpdateStatement = connection.prepareStatement("UPDATE ACCOUNTING.PAYMENTS SET DATE = ? , NUMBER = ?, PAYMENT = ?, TYPE = ?, AMOUNT = ? WHERE ID = ?");
            preparedUpdateStatement.setDate(1, Date.valueOf(payment.getDate()));
            preparedUpdateStatement.setInt(2, payment.getNumber());
            preparedUpdateStatement.setString(3, payment.getPayment());
            preparedUpdateStatement.setString(4, payment.getType());
            preparedUpdateStatement.setInt(5, payment.getSum());
            preparedUpdateStatement.setInt(6, payment.getID());

            preparedUpdateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deletePaymentFromDatabase(PaymentTab.Payment payment) {
        PreparedStatement preparedDeleteStatement;

        String deleteStatement = "DELETE FROM ACCOUNTING.PAYMENTS WHERE ID = ?";

        try {
            preparedDeleteStatement = connection.prepareStatement(deleteStatement);
            preparedDeleteStatement.setInt(1, payment.getID());
            preparedDeleteStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getPaymentsData() {
        Statement statement;

        String query = "SELECT * FROM ACCOUNTING.PAYMENTS";

        try {
            statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void backupDatabase() {
        try {
            Statement statement = connection.createStatement();

            statement.execute("SCRIPT NODATA TO 'init.sql'");

            statement.execute("SCRIPT TO 'backup.sql'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
