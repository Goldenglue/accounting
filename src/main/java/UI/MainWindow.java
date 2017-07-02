package UI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;


public class MainWindow {

    public static void launch(Stage primaryStage) {
        primaryStage.setTitle("Accounting application");
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(getPaymentsTab(), getMechanismsTab());
        Scene scene = new Scene(tabPane, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static Tab getPaymentsTab() {
        ObservableList<Payment> data = FXCollections.observableArrayList(
                new Payment("02.07.17", 101, "040-05 ООО \"СТС\" у Валеры", 6300, 6300)
        );

        Tab tab = new Tab();
        TableView<Payment> table = new TableView<>();
        table.setEditable(true);

        TableColumn<Payment, String> dateColumn = new TableColumn<>("Дата");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        dateColumn.setOnEditCommit(t -> (
                t.getTableView().getItems()
                        .get(t.getTablePosition().getRow()))
                .setDate(t.getNewValue()));

        TableColumn<Payment, Integer> numberColumn = new TableColumn<>("№ п/п");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));


        TableColumn<Payment, String> paymentColumn = new TableColumn<>();
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));
        paymentColumn.setMinWidth(200);

        TableColumn<Payment, String> unitColumn = new TableColumn<>("Ед. изм");
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));

        TableColumn<Payment, Integer> amountColumn = new TableColumn<>("Кол-во");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Payment, Integer> priceColumn = new TableColumn<>("Цена");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Payment, Integer> sumColumn = new TableColumn<>("Сумма");
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));

        table.setItems(data);
        table.getColumns().addAll(dateColumn, numberColumn, paymentColumn, unitColumn, amountColumn, priceColumn, sumColumn);
        tab.setContent(table);
        tab.setText("Платежи");
        tab.setClosable(false);
        return tab;
    }

    private static Tab getMechanismsTab() {
        Tab tab = new Tab();
        tab.setText("Механизмы");
        tab.setClosable(false);
        return tab;
    }

    public static class Payment {
        private final SimpleStringProperty date;
        private final SimpleIntegerProperty number;
        private final SimpleStringProperty payment;
        private final SimpleStringProperty unit = new SimpleStringProperty("Месяц");
        private final SimpleIntegerProperty amount = new SimpleIntegerProperty(1);
        private final SimpleIntegerProperty price;
        private final SimpleIntegerProperty sum;

        private Payment(String date, int number, String payment, int price, int sum) {
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
