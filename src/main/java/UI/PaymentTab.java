package UI;

import database.DataProcessing;
import dataclasses.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


abstract class PaymentTab extends Tab implements Loadable {
    TableView<Payment> table;
    static ObservableList<Payment> paymentObservableList = FXCollections.observableArrayList();

    protected abstract TableView setTableUp();

    @Override
    public List<Payment> loadFromDatabase(String period) {
        ResultSet resultSet = DataProcessing.getDataFromTable(period, "PAYMENTS");
        List<Payment> payments = new ArrayList<>();
        try {
            while (resultSet.next()) {
                payments.add(new PaymentBuilder()
                        .setDate(resultSet.getDate(2).toLocalDate())
                        .setPayment(resultSet.getString(3))
                        .setType(resultSet.getBoolean(4) ? PaymentType.RENT : PaymentType.TRANSPORTING)
                        .setSum(resultSet.getInt(5))
                        .setID(resultSet.getInt(1))
                        .setCashType(resultSet.getBoolean(6) ? CashType.CASH : CashType.CASHLESS)
                        .createPayment());
                payments.sort(Comparator.comparingInt(Payment::getID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    protected void createGUI() {}


}
