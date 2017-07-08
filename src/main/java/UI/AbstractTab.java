package UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;


abstract class AbstractTab extends Tab {
    protected TableView table;
    public static ObservableList<PaymentTab.Payment> observableList = FXCollections.observableArrayList();


    protected abstract TableView setTableUp();

    protected abstract void loadFromDatabase();

    protected void createGUI() {}


}
