package UI;

import database.DataProcessing;
import dataclasses.Renter;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RenterTab extends Tab {
    private ObservableList<Renter> renters = FXCollections.observableArrayList();

    RenterTab() {
        setTableUp();
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        vBox.getChildren().addAll(setTableUp());
        setContent(vBox);
        setText("Арендаторы");
        setClosable(false);
        renters.addAll(loadRentersFromDatabase());
    }

    private TableView<Renter> setTableUp() {
        TableView<Renter> renterTable = new TableView<>();
        renterTable.setEditable(true);

        TableColumn<Renter, String> renterTableColumn = new TableColumn<>("Информация об арендаторе");
        renterTableColumn.setCellValueFactory(new PropertyValueFactory<>("renter"));
        renterTableColumn.setPrefWidth(500);

        TableColumn<Renter, Integer> rentedCabinsTableColumn = new TableColumn<>("Арендованные вагончики");
        rentedCabinsTableColumn.setCellValueFactory(param -> {
            Renter renter = param.getValue();
            Integer cabinNumber = renter.getRentedCabins().isEmpty() ? null : renter.getRentedCabins().get(0);
            return new SimpleObjectProperty<>(cabinNumber);
        });
        rentedCabinsTableColumn.setCellFactory(param -> new ComboBoxTableCell<Renter, Integer>() {
            @Override
            public void startEdit() {
                super.startEdit();
                Renter renter = (Renter) getTableRow().getItem();
                getItems().setAll(renter.getRentedCabins());
            }
        });
        rentedCabinsTableColumn.setEditable(true);
        rentedCabinsTableColumn.setPrefWidth(200);

        TableColumn<Renter, Integer> renterDebtTableColumn = new TableColumn<>("Долг");
        renterDebtTableColumn.setCellValueFactory(new PropertyValueFactory<>("debt"));
        renterDebtTableColumn.setPrefWidth(60);

        TableColumn<Renter, String> renterPhoneTableColumn = new TableColumn<>("Номер телефона");
        renterPhoneTableColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        renterPhoneTableColumn.setPrefWidth(150);

        TableColumn<Renter, String> renterEmailTableColumn = new TableColumn<>("Email");
        renterEmailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        renterEmailTableColumn.setPrefWidth(100);

        renterTable.setItems(renters);
        renterTable.getColumns().addAll(renterTableColumn, rentedCabinsTableColumn, renterDebtTableColumn, renterPhoneTableColumn, renterEmailTableColumn);
        return renterTable;
    }

    private static List<Renter> loadRentersFromDatabase() {
        ResultSet set = DataProcessing.getDataFromTable("RENTERS_INFO", "RENTERS");
        List<Renter> renters = new ArrayList<>();
        try {
            while (set.next()) {
                Object[] objects = (Object[]) set.getArray(2).getArray();
                Integer[] numbers = new Integer[objects.length];
                for (int i = 0; i < objects.length; i++) {
                    numbers[i] = Integer.parseInt((String) objects[i]);
                }
                renters.add(new Renter(set.getString(1), numbers, set.getInt(3), set.getString(4), set.getString(5)));
            }
            return renters;
        } catch (SQLException e) {
            e.printStackTrace();
            return renters;
        }
    }
}
