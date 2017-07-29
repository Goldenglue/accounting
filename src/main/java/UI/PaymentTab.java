package UI;

import dataclasses.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;


abstract class PaymentTab extends Tab {
    TableView table;
    static ObservableList<Payment> paymentObservableList = FXCollections.observableArrayList();

    protected abstract TableView setTableUp();

    protected void loadFromDatabase(String period) {}

    protected void createGUI() {}


}
