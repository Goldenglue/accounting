package UI;

import database.DataProcessing;
import dataclasses.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class AllPaymentsTab extends PaymentTab {
    private final HBox hBox = new HBox();
    private final VBox vBox = new VBox();
    private TableColumn<Payment, LocalDate> dateColumn;
    private TableColumn<Payment, String> paymentColumn;
    private TableColumn<Payment, PaymentType> unitColumn;
    private TableColumn<Payment, CashType> cashTypeColumn;
    private TableColumn<Payment, Integer> sumColumn;
    private ComboBox<String> periodComboBox;
    private DatePicker datePicker;
    private TextField addPayment;
    public static List<String> rentersNames = DataProcessing.getRentersNames();
    private static Logger logger = LogManager.getLogger();

    AllPaymentsTab() {
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
        dateColumn.setEditable(false);
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
        paymentColumn.setEditable(false);

        unitColumn = new TableColumn<>("Тип");
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        unitColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(PaymentType.values())));
        unitColumn.setPrefWidth(90);
        unitColumn.setEditable(false);

        cashTypeColumn = new TableColumn<>("Вид расчета");
        cashTypeColumn.setCellValueFactory(new PropertyValueFactory<>("cashType"));
        cashTypeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(CashType.values())));
        cashTypeColumn.setPrefWidth(150);
        cashTypeColumn.setEditable(false);

        sumColumn = new TableColumn<>("Сумма");
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        sumColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        sumColumn.setPrefWidth(60);
        sumColumn.setEditable(false);

        table.setItems(paymentObservableList);
        table.getColumns().addAll(dateColumn, paymentColumn, unitColumn, cashTypeColumn, sumColumn);
        return table;
    }

    @Override
    protected void createGUI() {
        periodComboBox = new ComboBox<>();
        periodComboBox.getItems().addAll(DataProcessing.getAvailableTableNames("PAYMENTS"));
        periodComboBox.getSelectionModel().selectFirst();
        periodComboBox.setEditable(false);
        periodComboBox.setPrefWidth(100);

        paymentObservableList.clear();
        paymentObservableList.addAll(loadFromDatabase(periodComboBox.getValue()));

        final Button choosePeriod = new Button("Выбрать месяц");
        choosePeriod.setOnAction(action -> {
            paymentObservableList.clear();
            paymentObservableList.addAll(loadFromDatabase(periodComboBox.getValue()));
        });

        HBox selectionBox = new HBox();
        selectionBox.getChildren().addAll(periodComboBox, choosePeriod);

        datePicker = new DatePicker(LocalDate.now());
        datePicker.setMaxWidth(dateColumn.getPrefWidth());

        ContextMenu infoMenu = new ContextMenu();

        addPayment = new TextField();
        addPayment.setPrefWidth(paymentColumn.getPrefWidth());
        addPayment.setPromptText("Платеж");
        addPayment.setContextMenu(infoMenu);
        addPayment.setOnKeyReleased(event -> {
            infoMenu.getItems().clear();
            rentersNames.stream()
                    .filter(item -> item.toLowerCase().contains(addPayment.getText()) || item.contains(addPayment.getText()))
                    .limit(10)
                    .forEach(infoItem -> {
                        MenuItem menuItem = new MenuItem(infoItem);
                        menuItem.setOnAction(action -> addPayment.setText(infoItem));
                        infoMenu.getItems().add(menuItem);
                    });
            infoMenu.show(addPayment, Side.BOTTOM, 0, 0);
        });

        final ComboBox<PaymentType> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(PaymentType.values());
        typeComboBox.setEditable(false);
        typeComboBox.getSelectionModel().selectFirst();
        typeComboBox.setMaxWidth(unitColumn.getPrefWidth());

        final ComboBox<CashType> cashType = new ComboBox<>();
        cashType.getItems().addAll(CashType.values());
        cashType.getSelectionModel().selectFirst();
        cashType.setPrefWidth(cashTypeColumn.getPrefWidth());
        cashType.setEditable(false);

        final TextField addSum = new TextField();
        addSum.setPrefWidth(sumColumn.getPrefWidth());
        addSum.setPromptText("Сумма");

        final Button addButton = new Button("Добавить");
        addButton.setOnAction(action -> {
            ArrayList<Integer> paidCabinsNumbers;
            if (typeComboBox.getValue() == PaymentType.RENT) {
                Pair<Boolean, ArrayList<Integer>> result = createPaymentDialog(addPayment.getText(), Integer.parseInt(addSum.getText()));
                if (result.getKey()) {
                    paidCabinsNumbers = result.getValue();
                    Payment payment = new PaymentBuilder()
                            .setDate(datePicker.getValue())
                            .setPayment(addPayment.getText())
                            .setType(typeComboBox.getValue())
                            .setCashType(cashType.getValue())
                            .setSum(Integer.parseInt(addSum.getText()))
                            .setCabinsNumbers(paidCabinsNumbers)
                            .createPayment();
                    logger.info("Affected cabins numbers: " + paidCabinsNumbers);
                    payment.setID(DataProcessing.insertPayment(payment, periodComboBox.getValue()));
                    System.out.println(payment.toString());
                    paymentObservableList.add(payment);
                    addPayment.clear();
                    addSum.clear();
                } else {
                    addPayment.clear();
                    addSum.clear();
                }
            } else {
                Payment payment = new PaymentBuilder()
                        .setDate(datePicker.getValue())
                        .setPayment(addPayment.getText())
                        .setType(typeComboBox.getValue())
                        .setCashType(cashType.getValue())
                        .setSum(Integer.parseInt(addSum.getText()))
                        .createPayment();
                payment.setID(DataProcessing.insertPayment(payment, periodComboBox.getValue()));
                System.out.println(payment.toString());
                paymentObservableList.add(payment);
                addPayment.clear();
                addSum.clear();
            }
        });
        addButton.setDefaultButton(true);

        final Button removeButton = new Button("Отменить");
        removeButton.setOnAction(action -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Отмена платежа");
            alert.setHeaderText(null);
            if (table.getSelectionModel().getSelectedItem() != null) {
                alert.setContentText("Вы действительно хотите отменить этот платеж?" + "\n" + (table.getSelectionModel().getSelectedItem()).getPayment());
                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK)
                        .ifPresent(response -> {
                            Payment payment = table.getSelectionModel().getSelectedItem();
                            DataProcessing.deletePayment(payment, periodComboBox.getValue());
                            table.getItems().remove(payment);
                        });
            }
        });

        hBox.getChildren().addAll(datePicker, addPayment, typeComboBox, cashType, addSum, addButton, removeButton);

        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        vBox.getChildren().addAll(selectionBox, table, hBox);

    }

    private Pair<Boolean, ArrayList<Integer>> createPaymentDialog(String renter, Integer sum) {
        List<Integer> cabinsNumbers = new ArrayList<>();
        cabinsNumbers.addAll(Arrays.asList(DataProcessing.getRentedCabinsByRenter(renter)));
        Dialog<Pair<Boolean, ArrayList<Integer>>> dialog = new Dialog<>();
        dialog.setTitle("Платеж");

        ButtonType payType = new ButtonType("Оплатить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(payType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        ScrollPane pane = new ScrollPane();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        List<Cabin> cabins = cabinsNumbers
                .stream()
                .map(integer -> {
                    Cabin cabin = DataProcessing.getCabinByRenterAndNumber(renter, integer);
                    if (cabin != null) {
                        cabin.getPaymentDates().forEach(date -> {
                            if (!date.getMonth().equals(datePicker.getValue().getMonth())) {
                                cabin.setIsPaid(false);
                                System.out.println(cabin.isIsPaid());
                            }
                        });
                    }
                    return cabin;
                })
                .filter(cabin -> !cabin.isIsPaid())
                .collect(Collectors.toList());
        logger.info("Total amount of cabins is " + cabins.size());

        ObservableValue<Integer> alreadyPaid = new ReadOnlyObjectWrapper<>(cabins.stream()
                .filter(Cabin::isIsPaid)
                .mapToInt(Cabin::getRentPrice)
                .sum());


        StringProperty alreadyPaidAmount = new SimpleStringProperty(String.valueOf(alreadyPaid.getValue()));
        Label alreadyPaidLabel = new Label(alreadyPaidAmount.toString());
        alreadyPaidLabel.textProperty().bind(alreadyPaidAmount);

        cabins.forEach(cabin -> {
            int index = cabins.indexOf(cabin);
            Label amountToPay = new Label("К оплате за " + String.valueOf(cabin.getNumber()));
            Label text = new Label(String.valueOf(cabin.getRentPrice()));

            Button button = new Button("Оплатить " + String.valueOf(cabin.getNumber()));
            button.setOnAction(event -> {
                if (!cabin.isIsPaid()) {
                    alreadyPaidAmount.setValue(String.valueOf(Integer.parseInt(alreadyPaidAmount.getValue()) + cabin.getRentPrice()));
                    cabin.payForCabin(datePicker.getValue());
                    button.setDisable(true);
                }
            });
            grid.add(amountToPay, 0, index);
            grid.add(text, 1, index);
            grid.add(button, 2, index);
        });


        Label totalToPay = new Label(String.valueOf(cabins.stream()
                .mapToInt(Cabin::getRentPrice)
                .sum()));

        int index = cabins.size();
        grid.add(new Label("Всего к оплате:"), 0, index);
        grid.add(totalToPay, 1, index);
        grid.add(new Label("Предоставлено:"), 0, ++index);
        grid.add(new Label(String.valueOf(sum)), 1, index);
        grid.add(new Label("Оплачено:"), 0, ++index);
        grid.add(alreadyPaidLabel, 1, index);

        final TextField debtField = new TextField();
        debtField.setEditable(true);
        final Button countAsDebt = new Button("Зачислить долгом");
        countAsDebt.setOnAction(event -> {
            Renter renterByName = DataProcessing.getRenterByName(renter);
            assert renterByName != null;
            renterByName.setDebtAmount(renterByName.getDebtAmount() - Integer.parseInt(debtField.getText()));
            DataProcessing.updateRenterDebt(renterByName);
        });
        Integer possibleDebtAmountValue = cabins.stream()
                .filter(cabin -> !cabin.isIsPaid())
                .mapToInt(Cabin::getRentPrice)
                .sum() - sum;
        Label possibleDebtLabel = new Label(String.valueOf(possibleDebtAmountValue));
        grid.add(new Label("Потенциальный долг:"), 0, ++index);
        grid.add(possibleDebtLabel, 1, index);
        grid.add(debtField, 0, ++index);
        grid.add(countAsDebt, 1, index);


        grid.setPrefHeight(500);
        pane.setContent(grid);
        pane.setPrefViewportHeight(500);
        dialog.getDialogPane().setContent(pane);
        dialog.setResultConverter(result -> {
            if (result == payType) {
                return new Pair<>(true, cabins
                        .stream()
                        .filter(Cabin::isIsPaid)
                        .map(Cabin::getNumber)
                        .collect(Collectors.toCollection(ArrayList::new)));
            } else {
                return new Pair<>(false, new ArrayList<>());
            }
        });

        return dialog.showAndWait().get();
    }


    public static class LocalDateCellFactory extends TableCell<Payment, LocalDate> {
        final TextField textField = new TextField();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

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
