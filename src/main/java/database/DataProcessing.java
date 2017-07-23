package database;

import UI.CabinsTab;
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
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

            String insertStatement = "INSERT INTO PAYMENTS." + period + "(DATE, NUMBER, PAYMENT, TYPE, AMOUNT) VALUES(?,?,?,?,?)";

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

    public static int insertCabinIntoDatabase(CabinsTab.Cabin cabin, String databaseName) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO CABINS.ИЮЛ_2017_825(NAME,RENT_PRICE,CURRTEN_PAYMENT_AMOUNT,INVENTORY_PRICE,TRANSFER_DATE,RENTER,IS_PAID,INFO,NUMBER,PAYMENT_DATE) VALUES (?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            ps.setString(1, cabin.getName());
            ps.setInt(2, cabin.getRentPrice());
            ps.setInt(3, cabin.getCurrentPaymentAmount());
            ps.setInt(4, cabin.getInventoryPrice());
            if (cabin.getTransferDate() != null) {
                ps.setDate(5, Date.valueOf(formatter.format(cabin.getTransferDate())));
            } else {
                ps.setDate(5, null);
            }
            ps.setString(6, cabin.getRenter());
            ps.setBoolean(7, cabin.isIsPaid());
            ps.setString(8, cabin.getAdditionalInfo());
            ps.setInt(9, cabin.getNumber());
            ps.setInt(10, cabin.getPaymentDate());
            connection.setAutoCommit(false);
            ps.executeUpdate();
            ResultSet set = ps.getGeneratedKeys();
            int key = 0;
            while (set.next()) {
                key = set.getInt(1);
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
            statement.setString(1, info);
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

    public static List<String> getAvailableTableNames(String tableType) {
        List<String> names = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ?");
            statement.setString(1, tableType);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                names.add(set.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    public static ResultSet getDataFromTable(String table, String schema) {
        Statement selectDataStatement;
        String query = "SELECT * FROM " + schema + "." + table;

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

    public static void matchRenterWithCabins() {
        try {
            Statement st = connection.createStatement();
            ResultSet set = st.executeQuery("SELECT RENTER FROM RENTERS.RENTERS_INFO");
            List<String> rentersList = new ArrayList<>();
            while (set.next()) {
                rentersList.add(set.getString(1));
            }
            set = st.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'CABINS'");
            List<String> tableNames = new ArrayList<>();
            while (set.next()) {
                tableNames.add(set.getString(1));
            }
            /*System.out.println(rentersList);
            System.out.println(tableNames);*/
            /*ResultSet resultSet = st.executeQuery("SELECT NUMBER FROM CABINS." + tableNames.get(0) + " WHERE RENTER =" + "\'" + rentersList.get(0) + "\'");
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }*/
            rentersList.forEach(renter -> {
                List<String> tempList = new ArrayList<>();
                tableNames.forEach(table -> {
                    try {
                        ResultSet resultSet = st.executeQuery("SELECT NUMBER FROM CABINS." + table + " WHERE RENTER =" + "\'" + renter + "\'");
                        Map<String, List<String>> temp = new TreeMap<>();
                        while (resultSet.next()) {
                            tempList.add(resultSet.getString(1));
                        }

                        /*temp.put(renter, tempList);
                        System.out.println(table + " in table " + temp);*/
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                PreparedStatement pst = null;
                try {
                    pst = connection.prepareStatement("UPDATE RENTERS.RENTERS_INFO SET RENTED_CABINS = ? WHERE RENTER = ?");
                    pst.setArray(1, connection.createArrayOf("VARCHAR", tempList.toArray()));
                    pst.setString(2, renter);
                    pst.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
