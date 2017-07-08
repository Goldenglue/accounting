package UI;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;


abstract class AbstractTab extends Tab {
    protected TableView table;
    public static ObservableList observableList;


    protected abstract TableView setTableUp();

    protected abstract void loadFromDatabase();

    protected void createGUI() {}


}
