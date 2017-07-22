package UI;

import database.DataProcessing;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;


public class PaymentTab extends AbstractTab {
    private final HBox hBox = new HBox();
    private final VBox vBox = new VBox();
    private TableColumn<Payment, LocalDate> dateColumn;
    private TableColumn<Payment, Integer> numberColumn;
    private TableColumn<Payment, String> paymentColumn;
    private TableColumn<Payment, String> unitColumn;
    private TableColumn<Payment, Integer> sumColumn;
    private ComboBox<String> periodComboBox;

    PaymentTab() {
        table = setTableUp();
        createGUI();
        this.setContent(vBox);
        this.setText("Платежи");
        this.setClosable(false);
    }


    @Override
    protected TableView<Payment> setTableUp() {

        TableView<Payment> table = new TableView<>();
        table.setEditable(true);
        dateColumn = new TableColumn<>("Дата");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(column -> new LocalDateCellFactory());
        dateColumn.setOnEditCommit(editCommit -> {
            editCommit.getTableView().getItems()
                    .get(editCommit.getTablePosition().getRow())
                    .setDate(editCommit.getNewValue());
            DataProcessing.updatePayment(editCommit.getTableView().getItems().get(editCommit.getTablePosition().getRow()), periodComboBox.getValue());
        });
        dateColumn.setEditable(true);
        dateColumn.setPrefWidth(100);

        numberColumn = new TableColumn<>("№ п/п");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        /*numberColumn.setCellFactory(column -> new TableCell<Payment, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(String.valueOf(item));
                }
                setTextFill(Color.BLACK);
                setStyle("-fx-background-color: #f6ff4a");
            }
        });*/
        numberColumn.setOnEditCommit(t -> {
            t.getTableView().getItems()
                    .get(t.getTablePosition().getRow())
                    .setNumber(t.getNewValue());
            DataProcessing.updatePayment(t.getTableView().getItems().get(t.getTablePosition().getRow()), periodComboBox.getValue());
        });
        numberColumn.setPrefWidth(60);

        paymentColumn = new TableColumn<>("Платеж");
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));
        paymentColumn.setPrefWidth(400);
        paymentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        paymentColumn.setOnEditCommit(t -> {
            t.getTableView().getItems()
                    .get(t.getTablePosition().getRow())
                    .setPayment(t.getNewValue());
            DataProcessing.updatePayment(t.getTableView().getItems().get(t.getTablePosition().getRow()), periodComboBox.getValue());
        });

        unitColumn = new TableColumn<>("Тип");
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        unitColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        unitColumn.setOnEditCommit(t -> {
            t.getTableView().getItems()
                    .get(t.getTablePosition().getRow())
                    .setType(t.getNewValue());
            DataProcessing.updatePayment(t.getTableView().getItems().get(t.getTablePosition().getRow()), periodComboBox.getValue());
        });

        unitColumn.setPrefWidth(90);

        sumColumn = new TableColumn<>("Сумма");
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        sumColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        sumColumn.setOnEditCommit(t -> {
            t.getTableView().getItems()
                    .get(t.getTablePosition().getRow())
                    .setSum(t.getNewValue());
            DataProcessing.updatePayment(t.getTableView().getItems().get(t.getTablePosition().getRow()), periodComboBox.getValue());
        });
        sumColumn.setPrefWidth(60);

        table.setItems(paymentObservableList);
        table.getColumns().addAll(dateColumn, numberColumn, paymentColumn, unitColumn, sumColumn);
        return table;
    }

    @Override
    protected void loadFromDatabase(String period) {
        ResultSet resultSet = DataProcessing.getDataFromTable(period,"PAYMENTS" );
        try {
            while (resultSet.next()) {
                paymentObservableList.add(new Payment(resultSet.getDate(2).toLocalDate(), resultSet.getInt(3),
                        resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6), resultSet.getInt(1)));
                paymentObservableList.sort(Comparator.comparingInt(Payment::getID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void createGUI() {

        periodComboBox = new ComboBox<>();
        periodComboBox.getItems().addAll(DataProcessing.getAvailableTableNames("PAYMENTS"));
        periodComboBox.setEditable(false);
        periodComboBox.setPrefWidth(100);

        final Button choosePeriod = new Button("Выбрать месяц");
        choosePeriod.setOnAction(action -> {
            paymentObservableList.clear();
            loadFromDatabase(periodComboBox.getValue());
        });

        HBox selectionBox = new HBox();
        selectionBox.getChildren().addAll(periodComboBox, choosePeriod);

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setMaxWidth(dateColumn.getPrefWidth());

        final TextField addNumber = new TextField();
        addNumber.setPrefWidth(numberColumn.getPrefWidth());
        addNumber.setPromptText("№ п/п");

        List<String> info = DataProcessing.getRenters();

        ContextMenu infoMenu = new ContextMenu();

        final TextField addPayment = new TextField();
        addPayment.setPrefWidth(paymentColumn.getPrefWidth());
        addPayment.setPromptText("Платеж");
        addPayment.setContextMenu(infoMenu);
        addPayment.setOnKeyReleased(event -> {
            infoMenu.getItems().clear();
            info.stream()
                    .filter(item -> item.toLowerCase().contains(addPayment.getText()) || item.contains(addPayment.getText()))
                    .limit(10)
                    .forEach(infoItem -> {
                        MenuItem menuItem = new MenuItem(infoItem);
                        menuItem.setOnAction(action -> addPayment.setText(infoItem));
                        infoMenu.getItems().add(menuItem);
                    });
            infoMenu.show(addPayment, Side.BOTTOM, 0, 0);
        });

        final ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(
                "Аренда",
                "Механизм"
        );
        typeComboBox.setEditable(false);
        typeComboBox.setValue("Аренда");
        typeComboBox.setMaxWidth(unitColumn.getPrefWidth());

        final TextField addSum = new TextField();
        addSum.setPrefWidth(sumColumn.getPrefWidth());
        addSum.setPromptText("Сумма");

        final Button addButton = new Button("Добавить");
        addButton.setOnAction(action -> {
            Payment payment = new Payment(datePicker.getValue(), Integer.parseInt(addNumber.getText()), addPayment.getText(),
                    typeComboBox.getValue(), Integer.parseInt(addSum.getText()));
            payment.setID(DataProcessing.insertPaymentIntoDatabase(payment, periodComboBox.getValue()));
            paymentObservableList.add(payment);
            addNumber.clear();
            addPayment.clear();
            addSum.clear();
        });
        addButton.setDefaultButton(true);

        final Button removeButton = new Button("Удалить");
        removeButton.setOnAction(action -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Удаление строки из таблицы");
            alert.setHeaderText(null);
            alert.setContentText("Вы действительно хотите удалить эту строку?" + "\n" + ((Payment) table.getSelectionModel().getSelectedItem()).getPayment());
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                        Payment payment = (Payment) table.getSelectionModel().getSelectedItem();
                        DataProcessing.deletePaymentFromDatabase(payment, periodComboBox.getValue());
                        table.getItems().remove(payment);
                    });
        });

        hBox.getChildren().addAll(datePicker, addNumber, addPayment, typeComboBox, addSum, addButton, removeButton);

        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        vBox.getChildren().addAll(selectionBox, table, hBox);

    }


    public static class Payment {
        private final SimpleObjectProperty<LocalDate> date;
        private final SimpleIntegerProperty number;
        private final SimpleStringProperty payment;
        private final SimpleStringProperty type;
        private final SimpleIntegerProperty sum;
        private SimpleIntegerProperty ID;

        Payment(LocalDate date, int number, String payment, String type, int sum) {
            this.date = new SimpleObjectProperty<>(date);
            this.number = new SimpleIntegerProperty(number);
            this.payment = new SimpleStringProperty(payment);
            this.type = new SimpleStringProperty(type);
            this.sum = new SimpleIntegerProperty(sum);
            this.ID = new SimpleIntegerProperty(0);
        }

        Payment(LocalDate date, int number, String payment, String type, int sum, int ID) {
            this.date = new SimpleObjectProperty<>(date);
            this.number = new SimpleIntegerProperty(number);
            this.payment = new SimpleStringProperty(payment);
            this.type = new SimpleStringProperty(type);
            this.sum = new SimpleIntegerProperty(sum);
            this.ID = new SimpleIntegerProperty(ID);
        }


        public int getID() {
            return ID.get();
        }

        public SimpleIntegerProperty IDProperty() {
            return ID;
        }

        public void setID(int ID) {
            this.ID.set(ID);
        }

        public LocalDate getDate() {
            return date.getValue();
        }

        public void setDate(LocalDate date) {
            this.date.set(date);
        }

        public SimpleObjectProperty<LocalDate> dateProperty() {
            return date;
        }

        public int getNumber() {
            return number.get();
        }

        public void setNumber(int number) {
            this.number.set(number);
        }

        public SimpleIntegerProperty numberProperty() {
            return number;
        }

        public String getPayment() {
            return payment.get();
        }

        public void setPayment(String payment) {
            this.payment.set(payment);
        }

        public SimpleStringProperty paymentProperty() {
            return payment;
        }

        public String getType() {
            return type.get();
        }

        public void setType(String unit) {
            this.type.set(unit);
        }

        public SimpleStringProperty unitProperty() {
            return type;
        }

        public int getSum() {
            return sum.get();
        }

        public void setSum(int sum) {
            this.sum.set(sum);
        }

        public SimpleIntegerProperty sumProperty() {
            return sum;
        }

    }

    public static class LocalDateCellFactory extends TableCell<Payment, LocalDate> {
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
                setText(formatter.format(item));
                setGraphic(null);
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
