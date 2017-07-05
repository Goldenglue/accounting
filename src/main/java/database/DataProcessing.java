package database;

import UI.PaymentTab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataProcessing {
    private static Connection connection;

    public static void connectToDatabase() {
        try {
            Class.forName("org.h2.Driver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:h2:E:/Users/IvanOP/IdeaProjects/accounting/src/main/resources\\accountingdb","root","");
            System.out.println("Database connection successful");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertPaymentIntoDatabase(PaymentTab.Payment payment) {
        try {
            Statement statement;
            statement = connection.createStatement();
            String query = "INSERT INTO ACCOUNTING.PAYMENTS VALUES ('2017-05-03','Аренда','4000','VOT PRAYM TOLKO CHTO','3000')";
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
