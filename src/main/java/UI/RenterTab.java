package UI;

import database.DataProcessing;
import dataclasses.Renter;
import dataclasses.RenterBuilder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RenterTab extends Tab {
    private ObservableList<Renter> renters = FXCollections.observableArrayList();
    private TableView<Renter> renterTable;
    TableColumn<Renter, String> renterTableColumn;
    TableColumn<Renter, Integer> rentedCabinsTableColumn;

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

        renterTable = new TableView<>();
        renterTable.setEditable(true);


        renterTableColumn = new TableColumn<>("Информация об арендаторе");
        renterTableColumn.setCellValueFactory(new PropertyValueFactory<>("renter"));
        renterTableColumn.setPrefWidth(500);


        rentedCabinsTableColumn = new TableColumn<>("Арендованные вагончики");
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

        TableColumn<Renter, String> renterInfoTableColumn = new TableColumn<>("Информация");
        renterInfoTableColumn.setCellValueFactory(new PropertyValueFactory<>("info"));
        renterInfoTableColumn.setPrefWidth(200);

        renterTable.setItems(renters);
        renterTable.getColumns().addAll(renterTableColumn, rentedCabinsTableColumn, renterDebtTableColumn,
                renterPhoneTableColumn, renterEmailTableColumn, renterInfoTableColumn);
        return renterTable;
    }

    private void createGUI() {
        final TextField renter = new TextField();
        renter.setPromptText("Арендатор");
        renter.setPrefWidth(200);

        final TextField phone = new TextField();
        phone.setPromptText("Телефон");

        final TextField email = new TextField();
        email.setPromptText("Email");

        final TextField info = new TextField();
        info.setPromptText("Дополнительная информация");

        final Button addRenter = new Button("Добавить арендатора");
        addRenter.setOnAction(event -> {
            new RenterBuilder()
                    .setRenter(renter.getText())
                    .setPhoneNumber(phone.getText())
                    .setEmail(email.getText())
                    .setInfo(info.getText())
                    .createRenter();
        });
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
                renters.add(new RenterBuilder()
                        .setRenter(set.getString(1))
                        .setRentedCabins(numbers)
                        .setDebtAmount(set.getInt(3))
                        .setPhoneNumber(set.getString(4))
                        .setEmail(set.getString(5))
                        .setInfo(set.getString(6))
                        .createRenter());
            }
            return renters;
        } catch (SQLException e) {
            e.printStackTrace();
            return renters;
        }
    }
}
