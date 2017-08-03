package UI;

import com.sun.org.apache.regexp.internal.RE;
import database.DataProcessing;
import dataclasses.Loadable;
import dataclasses.Renter;
import dataclasses.RenterBuilder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RenterTab extends Tab implements Loadable {
    private ObservableList<Renter> renters = FXCollections.observableArrayList();
    private VBox vBox = new VBox();
    private TableView<Renter> renterTable;
    private TableColumn<Renter, String> renterTableColumn;
    private TableColumn<Renter, Integer> rentedCabinsTableColumn;
    private TableColumn<Renter, Integer> renterDebtTableColumn;
    private TableColumn<Renter, String> renterPhoneTableColumn;
    private TableColumn<Renter, String> renterEmailTableColumn;
    private TableColumn<Renter, String> renterInfoTableColumn;

    RenterTab() {
        setTableUp();
        createGUI();
        setContent(vBox);
        setText("Арендаторы");
        setClosable(false);
        renters.addAll(loadFromDatabase("RENTERS_INFO"));
    }

    private TableView<Renter> setTableUp() {

        renterTable = new TableView<>();
        renterTable.setEditable(true);

        renterTableColumn = new TableColumn<>("Арендатор");
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

        renterDebtTableColumn = new TableColumn<>("Долг");
        renterDebtTableColumn.setCellValueFactory(new PropertyValueFactory<>("debtAmount"));
        renterDebtTableColumn.setPrefWidth(60);

        renterPhoneTableColumn = new TableColumn<>("Номер телефона");
        renterPhoneTableColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        renterPhoneTableColumn.setPrefWidth(150);

        renterEmailTableColumn = new TableColumn<>("Email");
        renterEmailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        renterEmailTableColumn.setPrefWidth(100);

        renterInfoTableColumn = new TableColumn<>("Информация");
        renterInfoTableColumn.setCellValueFactory(new PropertyValueFactory<>("info"));
        renterInfoTableColumn.setPrefWidth(200);

        renterTable.setItems(renters);
        renterTable.getColumns().addAll(renterTableColumn, rentedCabinsTableColumn, renterDebtTableColumn,
                renterPhoneTableColumn, renterEmailTableColumn, renterInfoTableColumn);
        return renterTable;
    }

    private void createGUI() {
        final Button update = new Button("Обновить");
        update.setOnAction(event -> loadFromDatabase("RENTERS_INFO"));
        update.setPrefWidth(100);

        final TextField renter = new TextField();
        renter.setPromptText("Арендатор");
        renter.setPrefWidth(renterTableColumn.getPrefWidth());

        final TextField phone = new TextField();
        phone.setPromptText("Телефон");
        phone.setPrefWidth(renterPhoneTableColumn.getPrefWidth());

        final TextField email = new TextField();
        email.setPromptText("Email");
        email.setPrefWidth(renterEmailTableColumn.getPrefWidth());

        final TextField info = new TextField();
        info.setPromptText("Дополнительная информация");
        info.setPrefWidth(renterInfoTableColumn.getPrefWidth());

        final Button addRenter = new Button("Добавить арендатора");
        addRenter.setOnAction(event -> {
            Renter temp = new RenterBuilder()
                    .setRenter(renter.getText())
                    .setPhoneNumber(phone.getText())
                    .setEmail(email.getText())
                    .setInfo(info.getText())
                    .createRenter();
            DataProcessing.insertRenter(temp);
        });

        final Button removeRenter = new Button("Удалить арендатора");
        removeRenter.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Удаление арендатора");
            alert.setHeaderText(null);
            alert.setContentText("Удалить арендатора?" + "\n" + (renterTable.getSelectionModel().getSelectedItem()).getRenter());
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> DataProcessing.deleteRenter(renterTable.getSelectionModel().getSelectedItem()));
        });

        final Button showInfo = new Button("Показать информацию");
        showInfo.setOnAction(event -> {
            Pair<Boolean, Renter> result = showInfo(renterTable.getSelectionModel().getSelectedItem());
            if (result.getKey()) {
                DataProcessing.updateRenter(result.getValue());
            }
        });

        final Button rentSettings = new Button("Настройки аренды");

        HBox addRenterBox = new HBox();
        addRenterBox.getChildren().addAll(renter, phone, email, info, addRenter, removeRenter);

        HBox infoAndRentBox = new HBox();
        infoAndRentBox.getChildren().addAll(showInfo, rentSettings);

        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        vBox.getChildren().addAll(update, setTableUp(), addRenterBox, infoAndRentBox);
    }

    private Pair<Boolean, Renter> showInfo(Renter renter) {
        Dialog<Pair<Boolean, Renter>> dialog = new Dialog<>();
        dialog.setTitle("Информация об арендаторе");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ButtonType saveType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveType, cancelType);

        int rowIndex = 0;

        final TextArea renterArea = new TextArea();
        renterArea.setPrefRowCount(2);
        renterArea.setPrefWidth(300);
        renterArea.setWrapText(true);
        renterArea.setText(renter.getRenter());
        grid.add(new Label("Арендатель"), 0, rowIndex);
        grid.add(renterArea, 1, rowIndex);

        final ComboBox<Integer> renterCabins = new ComboBox<>();
        renterCabins.getItems().addAll(renter.getRentedCabins());
        renterCabins.getSelectionModel().selectFirst();
        grid.add(new Label("Арендованые вагончики"), 0, ++rowIndex);
        grid.add(renterCabins, 1, rowIndex);

        grid.add(new Label("Размер долга"), 0, ++rowIndex);
        grid.add(new Label(String.valueOf(renter.getDebtAmount())), 1, rowIndex);

        final TextField phoneNumber = new TextField(renter.getPhoneNumber());
        phoneNumber.setEditable(true);
        grid.add(new Label("Номер телефона"), 0, ++rowIndex);
        grid.add(phoneNumber, 1, rowIndex);

        final TextField email = new TextField(renter.getEmail());
        email.setEditable(true);
        grid.add(new Label("Email"), 0, ++rowIndex);
        grid.add(email, 1, rowIndex);

        final TextArea info = new TextArea();
        info.setPrefRowCount(2);
        info.setPrefWidth(300);
        info.setWrapText(true);
        info.setText(renter.getInfo());

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(param -> {
            if (param == saveType) {
                renter.setRenter(renterArea.getText());
                renter.setPhoneNumber(phoneNumber.getText());
                renter.setEmail(email.getText());
                renter.setInfo(info.getText());
                return new Pair<>(true, renter);
            } else {
                return new Pair<>(false, renter);
            }
        });

        return dialog.showAndWait().get();
    }

    @Override
    public List<Renter> loadFromDatabase(String s) {
        ResultSet set = DataProcessing.getDataFromTable("RENTERS_INFO", "RENTERS");
        List<Renter> renters = new ArrayList<>();
        try {
            while (set.next()) {
                renters.add(constructRenter(set));
            }
            return renters;
        } catch (SQLException e) {
            e.printStackTrace();
            return renters;
        }
    }

    public static Renter constructRenter(ResultSet set) {
        try {
            Object[] objects = (Object[]) set.getArray(2).getArray();
            Integer[] numbers = new Integer[objects.length];
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] != null && !Objects.equals(objects[i], "")) {
                    numbers[i] = Integer.parseInt((String) objects[i]);
                }
            }
            return new RenterBuilder()
                    .setRenter(set.getString(1))
                    .setRentedCabins(numbers)
                    .setDebtAmount(set.getInt(3))
                    .setPhoneNumber(set.getString(4))
                    .setEmail(set.getString(5))
                    .setInfo(set.getString(6))
                    .createRenter();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
