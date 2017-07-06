package UI;

import database.DataProcessing;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;


public class PaymentTab extends AbstractTab {
    private TableColumn<Payment, String> dateColumn;
    private TableColumn<Payment, Integer> numberColumn;
    private TableColumn<Payment, String> paymentColumn;
    private TableColumn<Payment, String> unitColumn;
    private TableColumn<Payment, Integer> sumColumn;
    private final HBox hBox = new HBox();
    private final VBox vBox = new VBox();

    PaymentTab() {
        observableList = FXCollections.observableArrayList();
        table = setTableUp();
        loadFromDatabase();
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
        dateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        dateColumn.setOnEditCommit(t -> (
                t.getTableView().getItems()
                        .get(t.getTablePosition().getRow()))
                .setDate(t.getNewValue())
        );
        dateColumn.setPrefWidth(80);

        numberColumn = new TableColumn<>("№ п/п");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        numberColumn.setOnEditCommit(t -> (
                t.getTableView().getItems()
                        .get(t.getTablePosition().getRow()))
                .setNumber(t.getNewValue())
        );
        numberColumn.setPrefWidth(60);

        paymentColumn = new TableColumn<>("Платеж");
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));
        paymentColumn.setPrefWidth(400);
        paymentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        paymentColumn.setOnEditCommit(t ->
                t.getTableView().getItems()
                        .get(t.getTablePosition().getRow())
                        .setPayment(t.getNewValue())
        );

        unitColumn = new TableColumn<>("Тип");
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        unitColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        unitColumn.setOnEditCommit(t ->
                t.getTableView().getItems()
                        .get(t.getTablePosition().getRow())
                        .setType(t.getNewValue()));
        unitColumn.setPrefWidth(90);

        sumColumn = new TableColumn<>("Сумма");
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        sumColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        sumColumn.setOnEditCommit(t ->
                t.getTableView().getItems()
                        .get(t.getTablePosition().getRow())
                        .setSum(t.getNewValue()));
        sumColumn.setPrefWidth(60);

        table.setItems(observableList);
        table.getColumns().addAll(dateColumn, numberColumn, paymentColumn, unitColumn, sumColumn);
        return table;
    }

    @Override
    protected void loadFromDatabase() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        ResultSet resultSet = DataProcessing.getPaymentsData();
        try {
            while (resultSet.next()) {
                observableList.add(new Payment(dateFormat.format(resultSet.getDate(1)), resultSet.getInt(2), resultSet.getString(3), resultSet.getInt(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void createGUI() {
        final TextField addDate = new TextField();
        addDate.setMaxWidth(dateColumn.getPrefWidth());
        addDate.setPromptText("Дата");

        TextField addNumber = new TextField();
        addNumber.setPrefWidth(numberColumn.getPrefWidth());
        addNumber.setPromptText("№ п/п");

        final TextField addPayment = new TextField();
        addPayment.setPrefWidth(paymentColumn.getPrefWidth());
        addPayment.setPromptText("Платеж");

        final TextField addUnit = new TextField();
        addUnit.setPrefWidth(unitColumn.getPrefWidth());
        addUnit.setText("Аренда");
        addUnit.setPromptText("Тип");


        final TextField addSum = new TextField();
        addSum.setPrefWidth(sumColumn.getPrefWidth());
        addSum.setPromptText("Сумма");

        final Button addButton = new Button("Добавить");
        addButton.setOnAction(action -> {
            Payment payment = new Payment(addDate.getText(), Integer.parseInt(addNumber.getText()), addPayment.getText(),
                    Integer.parseInt(addSum.getText()));
            DataProcessing.insertPaymentIntoDatabase(payment);
            observableList.add(payment);
            addDate.clear();
            addNumber.clear();
            addPayment.clear();
            addSum.clear();
        });

        hBox.getChildren().addAll(addDate, addNumber, addPayment, addUnit, addSum, addButton);

        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        vBox.getChildren().addAll(table, hBox);


    }

    public static class Payment {
        private final SimpleStringProperty date;
        private final SimpleIntegerProperty number;
        private final SimpleStringProperty payment;
        private final SimpleStringProperty type = new SimpleStringProperty("Аренда");
        private final SimpleIntegerProperty sum;


        public Payment(String date, int number, String payment, int sum) {
            this.date = new SimpleStringProperty(date);
            this.number = new SimpleIntegerProperty(number);
            this.payment = new SimpleStringProperty(payment);
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

        public String getType() {
            return type.get();
        }

        public SimpleStringProperty unitProperty() {
            return type;
        }

        public void setType(String unit) {
            this.type.set(unit);
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
