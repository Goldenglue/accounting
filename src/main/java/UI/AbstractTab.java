package UI;

import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;


abstract class AbstractTab extends Tab {
    protected TableView table;
    protected ObservableList observableList;


    protected abstract TableView setTableUp();

    protected void loadFromDatabase() { }

    protected void createGUI() { }


}
