package database;

import UI.PaymentTab;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.time.format.DateTimeFormatter;

public class DataProcessing {
    private static Connection connection;

    public static void connectToDatabase() {
        try {
            Class.forName("org.h2.Driver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:h2:~/data", "", "");
            System.out.println("Database connection successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        String deleteStatement = "DELETE FROM ACCOUNTING.PAYMENTS WHERE NUMBER = ? AND PAYMENT = ? AND AMOUNT = ?";

        try {
            preparedDeleteStatement = connection.prepareStatement(deleteStatement);
            preparedDeleteStatement.setInt(1, payment.getNumber());
            preparedDeleteStatement.setString(2, payment.getPayment());
            preparedDeleteStatement.setInt(3, payment.getSum());
            preparedDeleteStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Nullable
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

}
