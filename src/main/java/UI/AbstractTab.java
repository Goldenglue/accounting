package UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;


abstract class AbstractTab extends Tab {
    TableView table;
    static ObservableList<PaymentTab.Payment> paymentObservableList = FXCollections.observableArrayList();

    protected abstract TableView setTableUp();

    protected void loadFromDatabase(String period) {}

    protected void createGUI() {}


}
