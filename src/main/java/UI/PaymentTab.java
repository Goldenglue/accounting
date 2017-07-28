package UI;

import database.DataProcessing;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class PaymentTab extends AbstractTab {
    private final HBox hBox = new HBox();
    private final VBox vBox = new VBox();
    private TableColumn<Payment, LocalDate> dateColumn;
    private TableColumn<Payment, String> paymentColumn;
    private TableColumn<Payment, String> unitColumn;
    private TableColumn<Payment, Integer> sumColumn;
    private ComboBox<String> periodComboBox;
    private DatePicker datePicker;

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


        //DataProcessing.matchRenterWithCabins();
        table.setItems(paymentObservableList);
        table.getColumns().addAll(dateColumn, paymentColumn, unitColumn, sumColumn);
        return table;
    }

    @Override
    protected void loadFromDatabase(String period) {
        ResultSet resultSet = DataProcessing.getDataFromTable(period, "PAYMENTS");

        try {
            while (resultSet.next()) {
                paymentObservableList.add(new Payment(resultSet.getDate(2).toLocalDate(),
                        resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5), resultSet.getInt(1)));
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
        periodComboBox.getSelectionModel().selectFirst();
        periodComboBox.setEditable(false);
        periodComboBox.setPrefWidth(100);

        paymentObservableList.clear();
        loadFromDatabase(periodComboBox.getValue());

        final Button choosePeriod = new Button("Выбрать месяц");
        choosePeriod.setOnAction(action -> {
            paymentObservableList.clear();
            loadFromDatabase(periodComboBox.getValue());
        });

        HBox selectionBox = new HBox();
        selectionBox.getChildren().addAll(periodComboBox, choosePeriod);

        datePicker = new DatePicker(LocalDate.now());
        datePicker.setMaxWidth(dateColumn.getPrefWidth());

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
            if (typeComboBox.getValue().equals("Аренда")) {
                createPaymentDialog(addPayment.getText(), Integer.parseInt(addSum.getText()));
            }
            Payment payment = new Payment(datePicker.getValue(), addPayment.getText(),
                    typeComboBox.getValue(), Integer.parseInt(addSum.getText()));
            payment.setID(DataProcessing.insertPaymentIntoDatabase(payment, periodComboBox.getValue()));

            paymentObservableList.add(payment);
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

        hBox.getChildren().addAll(datePicker, addPayment, typeComboBox, addSum, addButton, removeButton);

        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        vBox.getChildren().addAll(selectionBox, table, hBox);

    }

    private void createPaymentDialog(String renter, Integer sum) {
        List<Integer> cabinsNumbers = new ArrayList<>();
        cabinsNumbers.addAll(Arrays.asList(DataProcessing.getRentedCabins(renter)));

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Платеж");

        ButtonType payType = new ButtonType("Pay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(payType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        ScrollPane pane = new ScrollPane();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        List<CabinsTab.Cabin> cabins = cabinsNumbers.stream()
                .map(integer -> DataProcessing.getCabinByRenterAndNumber(renter, integer))
                .collect(Collectors.toList());

        ObservableValue<Integer> alreadyPaidAmount = new ReadOnlyObjectWrapper<>(cabins.stream()
                .filter(CabinsTab.Cabin::isIsPaid)
                .mapToInt(CabinsTab.Cabin::getRentPrice)
                .sum());

        StringProperty alreadyPaidString = new SimpleStringProperty(String.valueOf(alreadyPaidAmount.getValue()));
        Label alreadyPaid = new Label(alreadyPaidString.toString());
        alreadyPaid.textProperty().bind(alreadyPaidString);

        cabins.forEach(cabin -> {
            int index = cabins.indexOf(cabin);
            Label amountToPay = new Label("К оплате за " + String.valueOf(cabin.getNumber()));
            Label text = new Label(String.valueOf(cabin.getRentPrice()));

            Button button = new Button("Оплатить " + String.valueOf(cabin.getNumber()));
            button.setOnAction(event -> {
                if (!cabin.isIsPaid()) {
                    alreadyPaidString.setValue(String.valueOf(Integer.parseInt(alreadyPaidString.getValue()) + cabin.getRentPrice()));
                    cabin.payForCabin(datePicker.getValue());
                    DataProcessing.updateCabinStatus(cabin.getSeries(), cabin);
                }
            });

            grid.add(amountToPay, 0, index);
            grid.add(text, 1, index);
            grid.add(button, 2, index);
        });


        Label totalToPay = new Label(String.valueOf(cabins.stream()
                .mapToInt(CabinsTab.Cabin::getRentPrice)
                .sum()));
        Button payForAll = new Button("Оплатить все");

        int index = cabins.size();
        grid.add(new Label("Всего к оплате:"), 0, index);
        grid.add(totalToPay, 1, index);
        grid.add(payForAll, 2, index);
        grid.add(new Label("Оплачено:"), 0, ++index);
        grid.add(alreadyPaid, 1, index);

        grid.setPrefHeight(500);
        pane.setContent(grid);
        pane.setPrefViewportHeight(500);
        dialog.getDialogPane().setContent(pane);

        dialog.show();

        Button pay = new Button("Оплатить");
        Button cancel = new Button("Отмена");
    }

    public static class Payment {
        private final SimpleObjectProperty<LocalDate> date;
        private final SimpleStringProperty payment;
        private final SimpleStringProperty type;
        private final SimpleIntegerProperty sum;
        private SimpleIntegerProperty ID;

        Payment(LocalDate date, String payment, String type, int sum) {
            this.date = new SimpleObjectProperty<>(date);
            this.payment = new SimpleStringProperty(payment);
            this.type = new SimpleStringProperty(type);
            this.sum = new SimpleIntegerProperty(sum);
            this.ID = new SimpleIntegerProperty(0);
        }

        Payment(LocalDate date, String payment, String type, int sum, int ID) {
            this.date = new SimpleObjectProperty<>(date);
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
