package UI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;


public class PaymentTab extends Tab {
    private TableView<PaymentTab.Payment> table;
    private TableColumn<Payment, String> dateColumn;
    private TableColumn<Payment, Integer> numberColumn;
    private TableColumn<Payment, String> paymentColumn;
    private TableColumn<Payment, String> unitColumn;
    private TableColumn<Payment, Integer> amountColumn;
    private TableColumn<Payment, Integer> priceColumn;
    private TableColumn<Payment, Integer> sumColumn;
    private ObservableList<Payment> paymentObservableList;
    private final HBox hBox = new HBox();
    private final VBox vBox = new VBox();

    PaymentTab() {
        paymentObservableList = FXCollections.observableArrayList(
                new PaymentTab.Payment("02.07.17", 101, "040-05 ООО \"СТС\" у Валеры", 6300, 6300)
        );
        table = setTableUp();
        createGUI();
        this.setContent(vBox);
        this.setText("Платежи");
        this.setClosable(false);
    }

    private TableView<Payment> setTableUp() {

        TableView<Payment> table = new TableView<>();
        table.setEditable(true);


        dateColumn = new TableColumn<>("Дата");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        dateColumn.setOnEditCommit(t -> (
                t.getTableView().getItems()
                        .get(t.getTablePosition().getRow()))
                        .setDate(t.getNewValue())
        );


        numberColumn = new TableColumn<>("№ п/п");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        numberColumn.setOnEditCommit(t -> (
                t.getTableView().getItems()
                        .get(t.getTablePosition().getRow()))
                        .setNumber(t.getNewValue())
        );


        paymentColumn = new TableColumn<>("Платеж");
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));
        paymentColumn.setMinWidth(200);
        paymentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        paymentColumn.setOnEditCommit(t ->
            t.getTableView().getItems()
                    .get(t.getTablePosition().getRow())
                        .setPayment(t.getNewValue())
        );


        unitColumn = new TableColumn<>("Ед. изм");
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        unitColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        unitColumn.setOnEditCommit(t ->
                t.getTableView().getItems()
                        .get(t.getTablePosition().getRow())
                        .setUnit(t.getNewValue()));


        amountColumn = new TableColumn<>("Кол-во");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        amountColumn.setOnEditCommit(t ->
                t.getTableView().getItems()
                        .get(t.getTablePosition().getRow())
                        .setAmount(t.getNewValue()));


        priceColumn = new TableColumn<>("Цена");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        priceColumn.setOnEditCommit(t ->
                t.getTableView().getItems()
                        .get(t.getTablePosition().getRow())
                        .setPrice(t.getNewValue()));


        sumColumn = new TableColumn<>("Сумма");
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        sumColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        sumColumn.setOnEditCommit(t ->
                t.getTableView().getItems()
                        .get(t.getTablePosition().getRow())
                        .setSum(t.getNewValue()));

        table.setItems(paymentObservableList);
        table.getColumns().addAll(dateColumn, numberColumn, paymentColumn, unitColumn, amountColumn, priceColumn, sumColumn);
        return table;
    }

    private void createGUI() {
        final TextField date = new TextField();
        date.setPrefWidth(dateColumn.getWidth());
        date.setPromptText("Дата");

        TextField number = new TextField();
        number.setPrefWidth(numberColumn.getPrefWidth());
        number.setPromptText("№ п/п");

        final TextField payment = new TextField();
        payment.setPrefWidth(paymentColumn.getWidth());
        payment.setPromptText("Платеж");

        final TextField unit = new TextField();
        unit.setText("Месяц");
        unit.setPromptText("Ед. изм.");

        final TextField amount = new TextField();
        amount.setText("1");
        amount.setPromptText("Кол-во");

        final TextField price = new TextField();
        price.setPromptText("Цена");

        final TextField sum = new TextField();
        sum.setPromptText("Сумма");

        final Button addButton = new Button("Добавить");
        addButton.setOnAction(action -> {
            paymentObservableList.add(new Payment(date.getText(), Integer.parseInt(number.getText()), payment.getText(),
                                        Integer.parseInt(price.getText()), Integer.parseInt(sum.getText())));
            date.clear();
            number.clear();
            payment.clear();
            price.clear();
            sum.clear();
        });

        hBox.getChildren().addAll(date, number, payment, unit, amount, price, sum, addButton);

        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        vBox.getChildren().addAll(table, hBox);


    }

    public static class Payment {
        private final SimpleStringProperty date;
        private final SimpleIntegerProperty number;
        private final SimpleStringProperty payment;
        private final SimpleStringProperty unit = new SimpleStringProperty("Месяц");
        private final SimpleIntegerProperty amount = new SimpleIntegerProperty(1);
        private final SimpleIntegerProperty price;
        private final SimpleIntegerProperty sum;

        public Payment(String date, int number, String payment, int price, int sum) {
            this.date = new SimpleStringProperty(date);
            this.number = new SimpleIntegerProperty(number);
            this.payment = new SimpleStringProperty(payment);
            this.price = new SimpleIntegerProperty(price);
            this.sum = new SimpleIntegerProperty(sum);
        }

        public String getDate() {
            return date.get();
        }

        public SimpleStringProperty dateProperty() {
            return date;
        }

        public void setDate(String date) {
            this.date.set(date);
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

        public String getPayment() {
            return payment.get();
        }

        public SimpleStringProperty paymentProperty() {
            return payment;
        }

        public void setPayment(String payment) {
            this.payment.set(payment);
        }

        public String getUnit() {
            return unit.get();
        }

        public SimpleStringProperty unitProperty() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit.set(unit);
        }

        public int getAmount() {
            return amount.get();
        }

        public SimpleIntegerProperty amountProperty() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount.set(amount);
        }

        public int getPrice() {
            return price.get();
        }

        public SimpleIntegerProperty priceProperty() {
            return price;
        }

        public void setPrice(int price) {
            this.price.set(price);
        }

        public int getSum() {
            return sum.get();
        }

        public SimpleIntegerProperty sumProperty() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum.set(sum);
        }
    }
}
