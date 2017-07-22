package UI;

import core.ExcelLoader;
import database.DataProcessing;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by IvanOP on 13.07.2017.
 */
public class CabinsTab extends AbstractTab {
    private final HBox hBox = new HBox();
    private final VBox vBox = new VBox();
    public static ObservableList<Cabin> cabinObservableList = FXCollections.observableArrayList();
    private TableColumn<Cabin, Integer> numberColumn;
    private TableColumn<Cabin, String> nameColumn;
    private TableColumn<Cabin, Integer> rentPriceColumn;
    private TableColumn<Cabin, Integer> currentPaymentColumn;
    private TableColumn<Cabin, Integer> inventoryPriceColumn;
    private TableColumn<Cabin, LocalDate> transferDateColumn;
    private TableColumn<Cabin, String> landlordInfoColumn;
    private TableColumn<Cabin, Integer> paymentDateColumn;
    private TableColumn<Cabin, Boolean> isPaidColumn;
    private TableColumn<Cabin, String> additionalInfoColumn;


    CabinsTab() {
        table = setTableUp();
        createGUI();
        setContent(vBox);
        setText("Бытовки");
        setClosable(false);
    }

    @Override
    protected TableView setTableUp() {
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

        landlordInfoColumn = new TableColumn<>("Информация об арендаторе");
        landlordInfoColumn.setCellValueFactory(new PropertyValueFactory<>("renter"));
        landlordInfoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        landlordInfoColumn.setPrefWidth(200);
        landlordInfoColumn.setEditable(true);

        paymentDateColumn = new TableColumn<>("Дата платежа");
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        paymentDateColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        paymentDateColumn.setPrefWidth(100);
        paymentDateColumn.setEditable(true);

        isPaidColumn = new TableColumn<>("Статус оплаты");
        isPaidColumn.setCellValueFactory(new PropertyValueFactory<>("isPaid"));
        isPaidColumn.setCellFactory(column -> new CheckBoxTableCell<>());
        isPaidColumn.setPrefWidth(100);
        isPaidColumn.setEditable(true);

        additionalInfoColumn = new TableColumn<>("Дополнительная информация");
        additionalInfoColumn.setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));
        additionalInfoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        additionalInfoColumn.setPrefWidth(200);
        additionalInfoColumn.setEditable(true);


        ExcelLoader.load();
        /*System.out.println(cabinObservableList.size());
        List<Cabin> temp = cabinObservableList.stream()
                .filter(cabin -> 2 > cabinObservableList.stream()
                        .filter(cabin1 -> cabin1.getNumber() == cabin.getNumber())
                        .count())
                .collect(Collectors.toList());
        System.out.println(cabinObservableList.size());
        cabinObservableList.forEach(cabin -> {

        });
        cabinObservableList = FXCollections.observableList(temp);
        System.out.println(cabinObservableList.size());*/
        table.setItems(cabinObservableList);
        table.getColumns().addAll(numberColumn, nameColumn, rentPriceColumn, currentPaymentColumn, inventoryPriceColumn, transferDateColumn, landlordInfoColumn, paymentDateColumn, isPaidColumn, additionalInfoColumn);
        return table;
    }

    @Override
    protected void createGUI() {
        List<String> tables = DataProcessing.getAvailableTableNames("CABINS");
        final ComboBox<String> period = new ComboBox<>();
        period.getItems().addAll(tables.stream()
                .map(s -> s.substring(s.length() - 8))
                .map(s -> s.replaceAll("_"," "))
                .collect(Collectors.toSet())
        );
        period.setEditable(false);
        period.setPrefWidth(100);

        final ComboBox<String> types = new ComboBox<>();
        types.getItems().addAll(tables.stream()
                .map(s -> s.substring(0,s.length() - 8))
                .map(s -> s.replaceAll("_", " "))
                .collect(Collectors.toSet())
        );
        types.setEditable(false);
        types.setPrefWidth(100);

        final Button selectPeriodTypes = new Button("Выбрать");
        selectPeriodTypes.setOnAction(event -> {

        });

        final TextField number = new TextField();
        number.setPromptText("№ п/п");

        final TextField name = new TextField();
        name.setPromptText("Наименовение объекта");

        final Button addButton = new Button("Добавить");
        addButton.setOnAction(action -> {
            Cabin cabin = new Cabin(Integer.parseInt(number.getText()), name.getText());
            cabinObservableList.add(cabin);
            number.clear();
            name.clear();
        });
        addButton.setDefaultButton(true);

        HBox selectionBox = new HBox();
        selectionBox.getChildren().addAll(period, types, selectPeriodTypes);

        hBox.getChildren().addAll(number, name, addButton);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        vBox.getChildren().addAll(selectionBox,table, hBox);
    }

    @Override
    protected void loadFromDatabase(String table) {
        ResultSet set = DataProcessing.getDataFromTable(table, "CABINS");

    }

    public static class Cabin {
        SimpleStringProperty series;
        SimpleIntegerProperty ID;
        SimpleIntegerProperty number;
        SimpleStringProperty name;
        SimpleIntegerProperty rentPrice;
        SimpleIntegerProperty currentPaymentAmount;
        SimpleIntegerProperty inventoryPrice;
        SimpleObjectProperty<LocalDate> transferDate;
        SimpleStringProperty renter;
        SimpleIntegerProperty paymentDate;
        SimpleBooleanProperty isPaid;
        SimpleStringProperty additionalInfo;

        public Cabin(int number, String name) {
            this.number = new SimpleIntegerProperty(number);
            this.name =  new SimpleStringProperty(name);
        }


        Cabin(int ID, int number, String name, int rentPrice, int currentPaymentAmount, int inventoryPrice, LocalDate transferDate, String renter, int paymentDate, boolean isPaid, String additionalInfo) {
            this.ID = new SimpleIntegerProperty(ID);
            this.number = new SimpleIntegerProperty(number);
            this.name = new SimpleStringProperty(name);
            this.rentPrice = new SimpleIntegerProperty(rentPrice);
            this.currentPaymentAmount = new SimpleIntegerProperty(currentPaymentAmount);
            this.inventoryPrice = new SimpleIntegerProperty(inventoryPrice);
            this.transferDate = new SimpleObjectProperty<>(transferDate);
            this.renter = new SimpleStringProperty(renter);
            this.paymentDate = new SimpleIntegerProperty(paymentDate);
            this.isPaid = new SimpleBooleanProperty(isPaid);
            this.additionalInfo = new SimpleStringProperty(additionalInfo);
        }
        public Cabin(int number, String name, int rentPrice, int currentPaymentAmount, int inventoryPrice, LocalDate transferDate, String renter, int paymentDate, boolean isPaid, String additionalInfo) {
            this.number = new SimpleIntegerProperty(number);
            this.name = new SimpleStringProperty(name);
            this.rentPrice = new SimpleIntegerProperty(rentPrice);
            this.currentPaymentAmount = new SimpleIntegerProperty(currentPaymentAmount);
            this.inventoryPrice = new SimpleIntegerProperty(inventoryPrice);
            this.transferDate = new SimpleObjectProperty<>(transferDate);
            this.renter = new SimpleStringProperty(renter);
            this.paymentDate = new SimpleIntegerProperty(paymentDate);
            this.isPaid = new SimpleBooleanProperty(isPaid);
            this.additionalInfo = new SimpleStringProperty(additionalInfo);
        }

        public int getNumber() {
            return number.get();
        }

        public SimpleIntegerProperty numberProperty() {
            return number;
        }

        public void setNumber(int number) {
            this.number.set(number);
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public int getRentPrice() {
            return rentPrice.get();
        }

        public SimpleIntegerProperty rentPriceProperty() {
            return rentPrice;
        }

        public void setRentPrice(int rentPrice) {
            this.rentPrice.set(rentPrice);
        }

        public int getCurrentPaymentAmount() {
            return currentPaymentAmount.get();
        }

        public SimpleIntegerProperty currentPaymentAmountProperty() {
            return currentPaymentAmount;
        }

        public void setCurrentPaymentAmount(int currentPaymentAmount) {
            this.currentPaymentAmount.set(currentPaymentAmount);
        }

        public int getInventoryPrice() {
            return inventoryPrice.get();
        }

        public SimpleIntegerProperty inventoryPriceProperty() {
            return inventoryPrice;
        }

        public void setInventoryPrice(int inventoryPrice) {
            this.inventoryPrice.set(inventoryPrice);
        }

        public LocalDate getTransferDate() {
            return transferDate.get();
        }

        public SimpleObjectProperty<LocalDate> transferDateProperty() {
            return transferDate;
        }

        public void setTransferDate(LocalDate transferDate) {
            this.transferDate.set(transferDate);
        }

        public String getRenter() {
            return renter.get();
        }

        public SimpleStringProperty renterProperty() {
            return renter;
        }

        public void setRenter(String renter) {
            this.renter.set(renter);
        }

        public int getPaymentDate() {
            return paymentDate.get();
        }

        public SimpleIntegerProperty paymentDateProperty() {
            return paymentDate;
        }

        public void setPaymentDate(int paymentDate) {
            this.paymentDate.set(paymentDate);
        }

        public boolean isIsPaid() {
            return isPaid.get();
        }

        public SimpleBooleanProperty isPaidProperty() {
            return isPaid;
        }

        public void setIsPaid(boolean isPaid) {
            this.isPaid.set(isPaid);
        }
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
            ((PaymentTab.Payment) this.getTableRow().getItem()).setDate(newValue);
        }
    }
}
