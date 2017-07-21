package database;

import UI.PaymentTab;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.RunScript;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataProcessing {
    private static Connection connection;
    private static Logger logger = LogManager.getLogger();


    public static void connectToDatabase() throws SQLException {
        try {
            Class.forName("org.h2.Driver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:h2:~/accounting/data;ifexists=true", "", "");
            logger.info("Successfully connected to database");
        } catch (SQLException e) {
            connection = DriverManager.getConnection("jdbc:h2:~/accounting/data;create=true", "", "");
            initDatabase();
            logger.info("Created new database");
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM_yyyy", new Locale("ru"));
        String tableName = formatter.format(date);
        String query = "CREATE TABLE IF NOT EXISTS PAYMENTS." + tableName + "("
                + "ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,"
                + "DATE DATE NOT NULL ,"
                + "NUMBER INT NOT NULL,"
                + "PAYMENT VARCHAR(256) NOT NULL,"
                + "TYPE VARCHAR(45) NOT NULL,"
                + "AMOUNT  INT NOT NULL"
                + ")";
        logger.info("Executing query: " + query);
        statement.executeUpdate(query);
        logger.info("Created database " + tableName);
    }

    public static int insertPaymentIntoDatabase(PaymentTab.Payment payment, String period) {
        try {
            PreparedStatement preparedInsertStatement;

            String insertStatement = "INSERT INTO PAYMENTS." + period + "(DATE, NUMBER, PAYMENT, TYPE, AMOUNT) VALUES" + "(?,?,?,?,?)";

            preparedInsertStatement = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = formatter.format(payment.getDate());
            connection.setAutoCommit(false);
            preparedInsertStatement.setDate(1, Date.valueOf(formattedDate));
            preparedInsertStatement.setInt(2, payment.getNumber());
            preparedInsertStatement.setString(3, payment.getPayment());
            preparedInsertStatement.setString(4, payment.getType());
            preparedInsertStatement.setInt(5, payment.getSum());
            logger.info("Inserting new value into database: " + preparedInsertStatement);
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

    public static void insertRenterIntoDatabase(String info) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO RENTERS.RENTERS_INFO(RENTER) VALUES(?)");
            statement.setString(1,info);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getRenters() {
        List<String> renters = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT RENTER FROM RENTERS.RENTERS_INFO");
            while (resultSet.next()) {
                renters.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return renters;
    }

    public static void updatePayment(PaymentTab.Payment payment, String period) {
        PreparedStatement preparedUpdateStatement;

        try {
            preparedUpdateStatement = connection.prepareStatement("UPDATE PAYMENTS." + period + " SET DATE = ? , NUMBER = ?, PAYMENT = ?, TYPE = ?, AMOUNT = ? WHERE ID = ?");
            preparedUpdateStatement.setDate(1, Date.valueOf(payment.getDate()));
            preparedUpdateStatement.setInt(2, payment.getNumber());
            preparedUpdateStatement.setString(3, payment.getPayment());
            preparedUpdateStatement.setString(4, payment.getType());
            preparedUpdateStatement.setInt(5, payment.getSum());
            preparedUpdateStatement.setInt(6, payment.getID());
            logger.info("Updating value in database with " + preparedUpdateStatement);
            preparedUpdateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deletePaymentFromDatabase(PaymentTab.Payment payment, String period) {
        PreparedStatement preparedDeleteStatement;

        String deleteStatement = "DELETE FROM PAYMENTS." + period + " WHERE ID = ?";
        logger.info("Removing: " + deleteStatement);

        try {
            preparedDeleteStatement = connection.prepareStatement(deleteStatement);
            preparedDeleteStatement.setInt(1, payment.getID());
            logger.info("Result of removing " + preparedDeleteStatement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAvailableTableNamesForPayments() {
        List<String> names = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PAYMENTS'");
            while (set.next()) {
                names.add(set.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    public static ResultSet getPaymentsDataFromCertainPeriod(String period) {
        Statement selectDataStatement;
        String query = "SELECT * FROM PAYMENTS." + "\"" + period + "\"";

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
