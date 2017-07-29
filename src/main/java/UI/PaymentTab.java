package UI;

import database.DataProcessing;
import dataclasses.Payment;
import dataclasses.PaymentBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;


abstract class PaymentTab extends Tab {
    TableView table;
    static ObservableList<Payment> paymentObservableList = FXCollections.observableArrayList();

    protected abstract TableView setTableUp();

    protected void loadFromDatabase(String period) {
        ResultSet resultSet = DataProcessing.getDataFromTable(period, "PAYMENTS");
        try {
            while (resultSet.next()) {
                paymentObservableList.add(new PaymentBuilder().setDate(resultSet.getDate(2).toLocalDate()).setPayment(resultSet.getString(3)).setType(resultSet.getString(4)).setSum(resultSet.getInt(5)).setID(resultSet.getInt(1)).createPayment());
                paymentObservableList.sort(Comparator.comparingInt(Payment::getID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void createGUI() {}


}
