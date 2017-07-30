package database;

import UI.CabinsTab;
import dataclasses.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.RunScript;
import utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        try {
            BufferedReader reader1 = Files.newBufferedReader(path);
            RunScript.execute(connection, reader1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateDatabaseOnNewMonth(LocalDate date) throws SQLException {
        Statement statement = connection.createStatement();
        String query = "CREATE TABLE IF NOT EXISTS PAYMENTS." + Utils.formatDateToMMM_yyyyString(date) + "("
                + "ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,"
                + "DATE DATE NOT NULL ,"
                + "PAYMENT VARCHAR(256) NOT NULL,"
                + "TYPE VARCHAR(45) NOT NULL,"
                + "AMOUNT  INT NOT NULL"
                + ")";
        logger.info("Executing query: " + query);
        statement.executeUpdate(query);
        logger.info("Created database " + date);
    }

    public static int insertPayment(Payment payment, String period) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO PAYMENTS." + period + "(DATE, PAYMENT, TYPE, AMOUNT,PAYMENT_TYPE) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            connection.setAutoCommit(false);
            ps.setDate(1, Date.valueOf(Utils.formatDateToyyyyMMdd(payment.getDate())));
            ps.setString(2, payment.getPayment());
            ps.setBoolean(3, payment.getType() == PaymentType.RENT);
            ps.setInt(4, payment.getSum());
            ps.setBoolean(5, payment.getCashType() == CashType.CASH);
            logger.info("Inserting new value into database: " + ps);
            ps.executeUpdate();

            ResultSet resultSet = ps.getGeneratedKeys();
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

    public static void updatePayment(Payment payment, String period) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE PAYMENTS." + period + " SET DATE = ? , PAYMENT = ?, TYPE = ?, AMOUNT = ?, PAYMENT_TYPE = ? WHERE ID = ?");
            ps.setDate(1, Date.valueOf(payment.getDate()));
            ps.setString(2, payment.getPayment());
            ps.setBoolean(3, payment.getType() == PaymentType.RENT);
            ps.setInt(4, payment.getSum());
            ps.setBoolean(5,payment.getCashType() == CashType.CASH);
            ps.setInt(6, payment.getID());
            logger.info("Updating value in database with " + ps);
            logger.info(ps.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deletePayment(Payment payment, String period) {
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

    public static int insertCabin(Cabin cabin) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO CABINS.CABINS(NUMBER,NAME,RENT_PRICE,CURRTEN_PAYMENT_AMOUNT,INVENTORY_PRICE" +
                    ",TRANSFER_DATE,RENTER,IS_PAID,PAYMENT_DATES,INFO,CURRENT_PAYMENT_DATE,PREVIOUS_RENTERS,SERIES) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, cabin.getNumber());
            ps.setString(2, cabin.getName());
            ps.setInt(3, cabin.getRentPrice());
            ps.setInt(4, cabin.getCurrentPaymentAmount());
            ps.setInt(5, cabin.getInventoryPrice());
            ps.setDate(6, cabin.getTransferDate() == null ? null : Date.valueOf(Utils.formatDateToyyyyMMdd(cabin.getTransferDate())));
            ps.setString(7, cabin.getRenter());
            ps.setBoolean(8, cabin.isIsPaid());
            ps.setArray(9, connection.createArrayOf("VARCHAR", cabin.getPaymentDates().toArray()));
            ps.setString(10, cabin.getAdditionalInfo());
            ps.setInt(11, cabin.getCurrentPaymentDate());
            ps.setArray(12, connection.createArrayOf("VARCHAR", cabin.getPreviousRenters().toArray()));
            ps.setString(13, "CABINS_" + cabin.getSeries().trim().replaceAll(" ","_"));
            connection.setAutoCommit(false);
            logger.info("inserting " + cabin.toString());
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
            return 1;
        }
    }

    public static void updateCabinPaymentStatus(Cabin cabin) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE CABINS.CABINS SET IS_PAID = ?, PAYMENT_DATES = ?, CURRENT_PAYMENT_DATE = ? WHERE ID = ?");
            ps.setBoolean(1, cabin.isIsPaid());
            ps.setArray(2, connection.createArrayOf("VARCHAR", cabin.getPaymentDates().stream().map(Utils::formatDateToyyyyMMdd).toArray()));
            ps.setInt(3, cabin.getCurrentPaymentDate());
            ps.setInt(4, cabin.getID());
            logger.info(cabin.getName());
            logger.info("Query: " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void updateCabinStockStatus(Cabin cabin, Boolean toStock) {
        if (toStock) {
            try {
                PreparedStatement ps = connection.prepareStatement("UPDATE CABINS.CABINS  SET CURRTEN_PAYMENT_AMOUNT = '0', TRANSFER_DATE = NULL, RENTER = ''," +
                        " IS_PAID = 'FALSE', PAYMENT_DATES = NULL , CURRENT_PAYMENT_DATE = '0', PREVIOUS_RENTERS = ?, STATUS = 'TRUE'");
                ps.setArray(1,connection.createArrayOf("VARCHAR",cabin.getPreviousRenters().toArray()));
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ResultSet getCabins(String series) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM CABINS.CABINS WHERE SERIES = ? ");
            ps.setString(1, series);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Cabin getCabinByRenterAndNumber(String renter, Integer number) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM CABINS.CABINS WHERE RENTER = ? AND NUMBER = ?");
            ps.setString(1, renter);
            ps.setInt(2, number);
            ResultSet set = ps.executeQuery();
            Cabin cabin = null;
            while (set.next()) {
                 cabin =  CabinsTab.constructCabin(set);
            }
            return cabin;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Integer[] getRentedCabinsByRenter(String renter) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT RENTED_CABINS FROM RENTERS.RENTERS_INFO WHERE RENTER = ?");
            st.setString(1, renter);
            ResultSet set = st.executeQuery();
            Array array = null;
            while (set.next()) {
                array = set.getArray(1);
            }
            Object[] objects = (Object[]) array.getArray();
            Integer[] integers = new Integer[objects.length];
            for (int i = 0; i < objects.length; i++) {
                integers[i] = Integer.parseInt((String) objects[i]);
            }
            return integers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void matchRenterToCabin(Renter renter, Cabin cabin) {

    }

    public static void deleteCabin(Cabin cabin) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM CABINS.CABINS WHERE ID = ?");
            ps.setInt(1,cabin.getID());
            ps.executeUpdate();
            logger.info("deleted cabin: " + cabin.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertRenter(Renter renter) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO RENTERS.RENTERS_INFO(RENTER,RENTED_CABINS,DEBT,PHONE,EMAIL,INFO) VALUES(?,?,?,?,?,?)");
            ps.setString(1, renter.getRenter());
            ps.setArray(2, connection.createArrayOf("VARCHAR(256)", renter.getRentedCabins().toArray()));
            ps.setInt(3, renter.getDebtAmount());
            ps.setString(4, renter.getPhoneNumber());
            ps.setString(5, renter.getEmail());
            ps.setString(6, renter.getInfo());
            ps.executeUpdate();
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
            if (!Files.exists(Paths.get("init.sql"))) {
                Files.createFile(Paths.get("init.sql"));
            }
            Statement statement = connection.createStatement();
            statement.execute("SCRIPT TO 'init.sql'");
            statement.execute("SCRIPT TO 'extraBackup.sql'");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


}
