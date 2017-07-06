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
            connection = DriverManager.getConnection("jdbc:h2:E:/Users/IvanOP/IdeaProjects/accounting/src/main/resources\\accountingdb", "root", "");
            System.out.println("Database connection successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void insertPaymentIntoDatabase(PaymentTab.Payment payment) {
        try {
            PreparedStatement preparedInsertStatement;

            String insertStatement = "INSERT INTO ACCOUNTING.PAYMENTS VALUES" + "(?,?,?,?,?)";

            preparedInsertStatement = connection.prepareStatement(insertStatement);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate;
            formattedDate = formatter.format(payment.getDate());


            preparedInsertStatement.setDate(1, Date.valueOf(formattedDate));
            preparedInsertStatement.setInt(2, payment.getNumber());
            preparedInsertStatement.setString(3, payment.getPayment());
            preparedInsertStatement.setString(4, payment.getType());
            preparedInsertStatement.setInt(5, payment.getSum());

            preparedInsertStatement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deletePaymentFromDatabase(PaymentTab.Payment payment) {
        PreparedStatement preparedDeleteStatement;

        String deleteStatement = "DELETE FROM ACCOUNTING.PAYMENTS WHERE NUMBER = ?";

        try {
            preparedDeleteStatement = connection.prepareStatement(deleteStatement);
            preparedDeleteStatement.setInt(1, payment.getNumber());
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
