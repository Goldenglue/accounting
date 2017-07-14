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
import java.util.ArrayList;
import java.util.List;

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
        Statement statement = connection.createStatement();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM_yyyy");
        String tableName = formatter.format(date);
        //String tableName = "\"" + formatter.format(date) + "\"";
        System.out.println(tableName);

        String query = "CREATE TABLE IF NOT EXISTS ACCOUNTING." + tableName + "("
                + "ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,"
                + "DATE DATE NOT NULL ,"
                + "NUMBER INT NOT NULL,"
                + "PAYMENT VARCHAR(256) NOT NULL,"
                + "TYPE VARCHAR(45) NOT NULL,"
                + "AMOUNT  INT NOT NULL"
                + ")";
        //String query1 = "CREATE UNIQUE INDEX IF NOT EXISTS " + tableName + "_ID_uindex ON ACCOUNTING." + tableName + "(ID)";

        connection.setAutoCommit(false);
        // Statement statement1 = connection.createStatement();
        System.out.println(statement.executeUpdate(query));
        //statement1.executeUpdate(query1);
        connection.commit();
        connection.setAutoCommit(true);

    }

    public static int insertPaymentIntoDatabase(PaymentTab.Payment payment, String period) {
        try {
            PreparedStatement preparedInsertStatement;

            String insertStatement = "INSERT INTO ACCOUNTING." + period + "(DATE, NUMBER, PAYMENT, TYPE, AMOUNT) VALUES" + "(?,?,?,?,?)";

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

    public static void updateValueInDatabase(PaymentTab.Payment payment, String period) {
        PreparedStatement preparedUpdateStatement;

        try {
            preparedUpdateStatement = connection.prepareStatement("UPDATE ACCOUNTING." + period+ " SET DATE = ? , NUMBER = ?, PAYMENT = ?, TYPE = ?, AMOUNT = ? WHERE ID = ?");
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

    public static void deletePaymentFromDatabase(PaymentTab.Payment payment, String period) {
        PreparedStatement preparedDeleteStatement;

        String deleteStatement = "DELETE FROM ACCOUNTING." + period +" WHERE ID = ?";

        try {
            preparedDeleteStatement = connection.prepareStatement(deleteStatement);
            preparedDeleteStatement.setInt(1, payment.getID());
            preparedDeleteStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAvailableTableNames() {
        List<String> names = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'ACCOUNTING'");
            while (set.next()) {
                String string = set.getString(1);
                string = string.replaceAll("\'", "");
                System.out.println(string);
                if (!(string.equals("PAYMENTS"))) {
                    names.add(string);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    public static ResultSet getPaymentsData(String period) {
        Statement selectDataStatement;
        String query = "SELECT * FROM ACCOUNTING." + "\"" + period + "\"";

        try {
            selectDataStatement = connection.createStatement();
            return selectDataStatement.executeQuery(query);
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
