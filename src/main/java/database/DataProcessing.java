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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataProcessing {
    private static Connection connection;
    private static Logger logger = LogManager.getLogger();
    private static List<String> cabinsTablesNames;


    public static void connectToDatabase() throws SQLException {
        try {
            Class.forName("org.h2.Driver").newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:h2:~/accounting/data;ifexists=true", "", "");
            logger.info("Successfully connected to database");
            cabinsTablesNames = getAvailableTableNames("CABINS");
        } catch (SQLException e) {
            connection = DriverManager.getConnection("jdbc:h2:~/accounting/data;create=true", "", "");
            initDatabase();
            cabinsTablesNames = getAvailableTableNames("CABINS");
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

    public static void createTableBasedOnLocalDate(LocalDate date) throws SQLException {
        Statement statement = connection.createStatement();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM_yyyy", new Locale("ru"));
        String tableName = formatter.format(date);
        String query = "CREATE TABLE IF NOT EXISTS PAYMENTS." + tableName + "("
                + "ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,"
                + "DATE DATE NOT NULL ,"
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

            String insertStatement = "INSERT INTO PAYMENTS." + period + "(DATE, PAYMENT, TYPE, AMOUNT) VALUES(?,?,?,?)";

            preparedInsertStatement = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = formatter.format(payment.getDate());
            connection.setAutoCommit(false);
            preparedInsertStatement.setDate(1, Date.valueOf(formattedDate));
            preparedInsertStatement.setString(2, payment.getPayment());
            preparedInsertStatement.setString(3, payment.getType());
            preparedInsertStatement.setInt(4, payment.getSum());
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
            PreparedStatement ps = connection.prepareStatement("INSERT INTO CABINS." + databaseName + "(NAME,RENT_PRICE,CURRTEN_PAYMENT_AMOUNT,INVENTORY_PRICE,TRANSFER_DATE,RENTER,IS_PAID,INFO,NUMBER,PAYMENT_DATE) VALUES (?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
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
            ps.setInt(10, cabin.getCurrentPaymentDate());
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
            preparedUpdateStatement = connection.prepareStatement("UPDATE PAYMENTS." + period + " SET DATE = ? , PAYMENT = ?, TYPE = ?, AMOUNT = ? WHERE ID = ?");
            preparedUpdateStatement.setDate(1, Date.valueOf(payment.getDate()));
            preparedUpdateStatement.setString(2, payment.getPayment());
            preparedUpdateStatement.setString(3, payment.getType());
            preparedUpdateStatement.setInt(4, payment.getSum());
            preparedUpdateStatement.setInt(5, payment.getID());
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
            if (!Files.exists(Paths.get("init.sql"))) {
                Files.createFile(Paths.get("init.sql"));
            }
            Statement statement = connection.createStatement();
            statement.execute("SCRIPT TO 'init.sql'");
        } catch (SQLException | IOException e) {
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
            rentersList.forEach(renter -> {
                List<String> tempList = new ArrayList<>();
                tableNames.forEach(table -> {
                    try {
                        ResultSet resultSet = st.executeQuery("SELECT NUMBER FROM CABINS." + table + " WHERE RENTER =" + "\'" + renter + "\'");
                        while (resultSet.next()) {
                            tempList.add(resultSet.getString(1));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                PreparedStatement pst;
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

    public static Integer[] getRentedCabins(String renter) {
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

    public static CabinsTab.Cabin getCabinByRenterAndNumber(String renter, Integer number) {
        List<CabinsTab.Cabin> cabins = new ArrayList<>();
        cabinsTablesNames.forEach(name -> cabins.addAll(CabinsTab.loadCabinsFromDatabase(name)));
        return cabins.stream()
                .filter(cabin -> cabin.getNumber() == number && cabin.getRenter().equals(renter))
                .findFirst()
                .get();
    }

    public static void updateCabinStatus(String table, CabinsTab.Cabin cabin) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE CABINS." + table + " SET IS_PAID = ?, PAYMENT_DATES = ?, CURRENT_PAYMENT_DATE = ? WHERE ID = ?");
            ps.setBoolean(1, cabin.isIsPaid());
            ps.setArray(2, connection.createArrayOf("INTEGER", cabin.getPaymentDates().toArray()));
            ps.setInt(3, cabin.getCurrentPaymentDate());
            ps.setInt(4, cabin.getID());
            logger.info(cabin.getName());
            logger.info("Query: " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
