package UI;

import database.DataProcessing;
import dataclasses.Cabin;
import dataclasses.CabinBuilder;
import dataclasses.Payment;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;
import utils.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IvanOP on 13.07.2017.
 */
public class CabinsTab extends Tab {
    private final HBox hBox = new HBox();
    private final VBox vBox = new VBox();
    public static ObservableList<Cabin> cabinObservableList = FXCollections.observableArrayList();
    private final TableView<Cabin> table;
    private TableColumn<Cabin, Integer> numberColumn;
    private TableColumn<Cabin, String> nameColumn;
    private TableColumn<Cabin, Integer> rentPriceColumn;
    private TableColumn<Cabin, Integer> currentPaymentColumn;
    private TableColumn<Cabin, Integer> inventoryPriceColumn;
    private TableColumn<Cabin, LocalDate> transferDateColumn;
    private TableColumn<Cabin, String> renterInfoColumn;
    private TableColumn<Cabin, Boolean> isPaidColumn;
    private TableColumn<Cabin, Integer> paymentDateColumn;
    private TableColumn<Cabin, String> additionalInfoColumn;
    private final Button toStock = new Button("На склад");

    {
        toStock.setOnAction(event -> {

        });
    }

    CabinsTab() {
        table = setTableUp();
        createGUI();
        setContent(vBox);
        setText("Бытовки");
        setClosable(false);
    }

    private TableView<Cabin> setTableUp() {
        TableView<Cabin> table = new TableView<>();
        table.setEditable(true);

        numberColumn = new TableColumn<>("№ п/п");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        numberColumn.setPrefWidth(60);
        numberColumn.setEditable(true);

        nameColumn = new TableColumn<>("Наименование");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setPrefWidth(200);
        nameColumn.setEditable(true);

        rentPriceColumn = new TableColumn<>("Стоимость аренды");
        rentPriceColumn.setCellValueFactory(new PropertyValueFactory<>("rentPrice"));
        rentPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        rentPriceColumn.setPrefWidth(100);
        rentPriceColumn.setEditable(true);

        currentPaymentColumn = new TableColumn<>("Сумма текущего платежа");
        currentPaymentColumn.setCellValueFactory(new PropertyValueFactory<>("currentPaymentAmount"));
        currentPaymentColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        currentPaymentColumn.setPrefWidth(150);
        currentPaymentColumn.setEditable(true);

        inventoryPriceColumn = new TableColumn<>("Инвентарная стоимость с учётом амортизации");
        inventoryPriceColumn.setCellValueFactory(new PropertyValueFactory<>("inventoryPrice"));
        inventoryPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        inventoryPriceColumn.setPrefWidth(200);
        inventoryPriceColumn.setEditable(true);

        transferDateColumn = new TableColumn<>(" Дата передачи по АКТУ");
        transferDateColumn.setCellValueFactory(new PropertyValueFactory<>("transferDate"));
        transferDateColumn.setCellFactory(column -> new LocalDateCellFactory());
        transferDateColumn.setPrefWidth(120);
        transferDateColumn.setEditable(true);

        renterInfoColumn = new TableColumn<>("Информация об арендаторе");
        renterInfoColumn.setCellValueFactory(new PropertyValueFactory<>("renter"));
        renterInfoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        renterInfoColumn.setPrefWidth(200);
        renterInfoColumn.setEditable(true);

        isPaidColumn = new TableColumn<>("Статус оплаты");
        isPaidColumn.setCellValueFactory(new PropertyValueFactory<>("isPaid"));
        isPaidColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        isPaidColumn.setPrefWidth(100);
        isPaidColumn.setEditable(true);

        ObservableList<ArrayList<LocalDate>> cellData = FXCollections.observableArrayList();
        cellData.setAll(cabinObservableList.stream()
                .map(Cabin::getPaymentDates)
                .collect(Collectors.toList()));
        TableColumn<Cabin, LocalDate> paymentDatesColumn = new TableColumn<>("Даты платежей");
        paymentDatesColumn.setCellValueFactory(param -> {
            Cabin cabin = param.getValue();
            LocalDate date = cabin.getPaymentDates().size() > 0 ? cabin.getPaymentDates().get(0) : null;
            return new SimpleObjectProperty<>(date);
        });
        paymentDatesColumn.setCellFactory(param -> new ComboBoxTableCell<Cabin, LocalDate>() {
            @Override
            public void startEdit() {
                super.startEdit();
                Cabin cabin = (Cabin) getTableRow().getItem();
                getItems().setAll(cabin.getPaymentDates());
            }
        });
        paymentDatesColumn.setEditable(true);
        paymentDatesColumn.setPrefWidth(100);

        additionalInfoColumn = new TableColumn<>("Дополнительная информация");
        additionalInfoColumn.setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));
        additionalInfoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        additionalInfoColumn.setPrefWidth(200);
        additionalInfoColumn.setEditable(true);

        paymentDateColumn = new TableColumn<>("Дата платежа");
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("currentPaymentDate"));
        paymentDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        paymentDateColumn.setPrefWidth(100);
        paymentDateColumn.setEditable(true);

        TableColumn<Cabin, LocalDate> previousRentersColumn = new TableColumn<>("Предыдущие арендатели");
        previousRentersColumn.setCellFactory(ComboBoxTableCell.forTableColumn());
        previousRentersColumn.setEditable(false);
        previousRentersColumn.setPrefWidth(100);

        table.setItems(cabinObservableList);
        table.getColumns().addAll(numberColumn, nameColumn, rentPriceColumn, currentPaymentColumn, inventoryPriceColumn, transferDateColumn, renterInfoColumn, paymentDatesColumn,
                isPaidColumn, additionalInfoColumn, paymentDateColumn, previousRentersColumn);
        return table;
    }

    private void createGUI() {
        List<String> tables = new ArrayList<>();
        ResultSet dataFromTable = DataProcessing.getDataFromTable("AVAILABLE_SERIES", "CABINS");
        try {
            while (dataFromTable.next()) {
                tables.add(dataFromTable.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final ComboBox<String> types = new ComboBox<>();
        types.getItems().addAll(tables.stream()
                .map(s -> s.replaceAll("_", " "))
                .map(s -> s.replaceAll("CABINS", ""))
                .collect(Collectors.toSet())
        );
        types.setEditable(false);
        types.setPrefWidth(100);

        final Button selectPeriodTypes = new Button("Выбрать");
        selectPeriodTypes.setOnAction(event -> {
            cabinObservableList.clear();
            cabinObservableList.addAll(loadCabinsFromDatabase(("CABINS" + types.getValue().replaceAll(" ", " _")).replaceAll(" ", "")));
        });

        final TextField number = new TextField();
        number.setPromptText("№ п/п");

        final TextField name = new TextField();
        name.setPromptText("Наименовение объекта");

        final Button addButton = new Button("Добавить");
        addButton.setOnAction(action -> {
            Cabin cabin = new CabinBuilder()
                    .setNumber(Integer.parseInt(number.getText()))
                    .setName(name.getText())
                    .setSeries(types.getValue())
                    .createCabin();
            cabin.setID(DataProcessing.insertCabinIntoDatabase(cabin));
            cabinObservableList.add(cabin);
            number.clear();
            name.clear();
        });
        addButton.setDefaultButton(true);

        final Button removeButton = new Button("Удалить");
        removeButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Удаление строки из таблицы");
            alert.setHeaderText(null);
            alert.setContentText("Вы действительно хотите удалить эту строку?" + "\n" + (table.getSelectionModel().getSelectedItem()).getName());
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                        Cabin cabin = table.getSelectionModel().getSelectedItem();
                        DataProcessing.deleteCabinFromDatabase(cabin);
                        table.getItems().remove(cabin);
                    });
        });

        final Button showInfo = new Button("Подробная информация");
        showInfo.setOnAction(event -> System.out.println(showInfo(table.getSelectionModel().getSelectedItem()).getKey()));


        HBox addRemoveBox = new HBox();
        addRemoveBox.getChildren().addAll(types, selectPeriodTypes);
        hBox.getChildren().addAll(number, name, addButton, removeButton);

        HBox manipulationsBox = new HBox();
        manipulationsBox.getChildren().addAll(showInfo, toStock);

        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        vBox.getChildren().addAll(addRemoveBox, table, hBox, manipulationsBox);
    }

    private Pair<Boolean, Cabin> showInfo(Cabin cabin) {
        Dialog<Pair<Boolean, Cabin>> dialog = new Dialog<>();
        dialog.setTitle(String.valueOf(cabin.getNumber()));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ButtonType saveType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelType = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveType, cancelType);

        int rowIndex = 0;


        final TextArea name = new TextArea(cabin.getName());
        name.setPrefRowCount(2);
        name.setWrapText(true);
        name.setPrefWidth(300);
        grid.add(new Label("Название:"), 0, rowIndex);
        grid.add(name, 1, rowIndex);


        final TextField rentPrice = new TextField(String.valueOf(cabin.getRentPrice()));
        grid.add(new Label("Стоимость аренды:"), 0, ++rowIndex);
        grid.add(rentPrice, 1, rowIndex);

        final TextField currentPaymentAmount = new TextField(String.valueOf(cabin.getCurrentPaymentAmount()));
        grid.add(new Label("Сумма текущего платежа:"), 0, ++rowIndex);
        grid.add(currentPaymentAmount, 1, rowIndex);

        final TextField inventoryPrice = new TextField(String.valueOf(cabin.getInventoryPrice()));
        grid.add(new Label("Инвентарная стоимость:"), 0, ++rowIndex);
        grid.add(inventoryPrice, 1, rowIndex);

        grid.add(new Label("Дата передачи по акту:"), 0, ++rowIndex);
        grid.add(new Label(cabin.getTransferDate() == null ? "" : Utils.formatDateToyyyyMMdd(cabin.getTransferDate())), 1, rowIndex);

        final TextArea renter = new TextArea(cabin.getRenter());
        grid.add(new Label("Арендатор:"), 0, ++rowIndex);
        renter.setPrefRowCount(2);
        renter.setWrapText(true);
        renter.setEditable(false);
        renter.setPrefWidth(300);
        grid.add(renter, 1, rowIndex);

        final CheckBox isPaid = new CheckBox() {
            @Override
            public void arm() {
            }
        };
        isPaid.setSelected(cabin.isIsPaid());
        grid.add(new Label("Статус оплаты:"), 0, ++rowIndex);
        grid.add(isPaid, 1, rowIndex);

        final ComboBox<LocalDate> paymentDates = new ComboBox<>();
        paymentDates.getItems().addAll(cabin.getPaymentDates());
        paymentDates.getSelectionModel().selectFirst();
        grid.add(new Label("Даты оплаты"), 0, ++rowIndex);
        grid.add(paymentDates, 1, rowIndex);

        final TextArea info = new TextArea(cabin.getAdditionalInfo());
        info.setPrefRowCount(2);
        info.setWrapText(true);
        info.setEditable(true);
        info.setPrefWidth(300);
        grid.add(new Label("Дополнительная информация"), 0, ++rowIndex);
        grid.add(info, 1, rowIndex);

        grid.add(new Label("Дата текущей оплаты"), 0, ++rowIndex);
        grid.add(new Label(String.valueOf(cabin.getCurrentPaymentDate())), 1, rowIndex);

        final ComboBox<String> previousRenters = new ComboBox<>();
        previousRenters.getItems().addAll(cabin.getPreviousRenters());
        previousRenters.getSelectionModel().selectFirst();
        grid.add(new Label("Предыдущие арендаторы"), 0, ++rowIndex);
        grid.add(previousRenters, 1, rowIndex);

        grid.add(toStock, 0, ++rowIndex);


        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(value -> {
            if (value == saveType) {
                cabin.setName(name.getText());
                if (rentPrice.getText().matches("^[0-9]*$")) {
                    cabin.setRentPrice(Integer.parseInt(rentPrice.getText()));
                }
                if (currentPaymentAmount.getText().matches("^[0-9]*$")) {
                    cabin.setCurrentPaymentAmount(Integer.parseInt(currentPaymentAmount.getText()));
                }
                if (inventoryPrice.getText().matches("^[0-9]*$")) {
                    cabin.setInventoryPrice(Integer.parseInt(inventoryPrice.getText()));
                }

                cabin.setAdditionalInfo(info.getText());
                return new Pair<>(true, cabin);
            } else {
                return new Pair<>(false, cabin);
            }
        });

        //Optional<Pair<Boolean, Cabin>> result = dialog.showAndWait();
        return dialog.showAndWait().get();
    }

    public static List<Cabin> loadCabinsFromDatabase(String series) {
        ResultSet set = DataProcessing.getCabinsFromDatabase(series);
        List<Cabin> cabins = new ArrayList<>();
        try {
            while (set.next()) {
                Object[] objects = (Object[]) set.getArray(9).getArray();
                String[] dates = new String[objects.length];
                for (int i = 0; i < objects.length; i++) {
                    if (objects[i] != null && objects[i] != "0") {
                        dates[i] = (String) objects[i];
                    } else {
                        dates[i] = "0";
                    }
                }
                Object[] renters = (Object[]) set.getArray(12).getArray();
                String[] strings = new String[renters.length];
                for (int i = 0; i < renters.length; i++) {
                    strings[i] = renters[i].toString();
                }
                cabins.add(new CabinBuilder()
                        .setNumber(set.getInt(1))
                        .setName(set.getString(2))
                        .setRentPrice(set.getInt(3))
                        .setCurrentPaymentAmount(set.getInt(4))
                        .setInventoryPrice(set.getInt(5))
                        .setTransferDate(set.getDate(6) != null ? set.getDate(6).toLocalDate() : null)
                        .setRenter(set.getString(7))
                        .setIsPaid(set.getBoolean(8))
                        .setPaymentDates(dates)
                        .setAdditionalInfo(set.getString(10))
                        .setCurrentPaymentDate(set.getInt(11))
                        .setPreviousRenters(strings)
                        .setSeries(set.getString(13))
                        .setID(set.getInt(14))
                        .createCabin());
            }
            cabins.sort(Comparator.comparingInt(Cabin::getNumber));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cabins;
    }

    public static class LocalDateCellFactory extends TableCell<Cabin, LocalDate> {
        final TextField textField = new TextField();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        LocalDateCellFactory() {
            textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (!isNowFocused) {
                    processEdit();
                }
            });
            textField.setOnAction(event -> processEdit());
        }

        private void processEdit() {
            String text = textField.getText();
            commitEdit(LocalDate.parse(text, formatter));
        }

        @Override
        protected void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else if (isEditing()) {
                setText(null);
                textField.setText(formatter.format(item));
                setGraphic(textField);
            } else {
                if (item != null) {
                    setText(formatter.format(item));
                    setGraphic(null);
                }
            }
        }

        @Override
        public void startEdit() {
            super.startEdit();
            textField.setText(formatter.format(getItem()));
            textField.selectAll();
            setGraphic(textField);
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(formatter.format(getItem()));
            setGraphic(null);
        }

        @Override
        public void commitEdit(LocalDate newValue) {
            super.commitEdit(newValue);
            ((Payment) this.getTableRow().getItem()).setDate(newValue);
        }
    }
}
