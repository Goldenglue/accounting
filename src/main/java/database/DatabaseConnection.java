package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    public static void main(String[] args) {
        try {
            Class.forName("org.h2.Driver").newInstance();
            Connection connection = DriverManager.getConnection("jdbc:h2:E:/Users/IvanOP/IdeaProjects/accounting/src/main/resources\\accountingdb","root","");
            Statement statement;
            statement = connection.createStatement();
            statement.execute("INSERT INTO ACCOUNTING.PAYMENTS VALUES ('2017-05-03','Аренда','4000','VOT PRAYM TOLKO CHTO','3000')");

        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
