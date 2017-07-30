package UI;

import dataclasses.Payment;
import dataclasses.PaymentType;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.time.LocalDate;

public class MechanismsPaymentTab extends PaymentTab {

    MechanismsPaymentTab() {
        table = setTableUp();
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 0, 0, 10));
        vBox.getChildren().addAll(table);
        this.setContent(vBox);
        this.setText("Платежи за транспорт");
        this.setClosable(false);
    }

    @Override
    protected TableView<Payment> setTableUp() {
        TableView<Payment> table = new TableView<>();
        table.setEditable(false);

        TableColumn<Payment, LocalDate> dateColumn = new TableColumn<>("Дата");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(column -> new AllPaymentsTab.LocalDateCellFactory());
        dateColumn.setPrefWidth(100);

        TableColumn<Payment, Integer> numberColumn = new TableColumn<>("№ п/п");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        numberColumn.setPrefWidth(60);

        TableColumn<Payment, String> paymentColumn = new TableColumn<>("Платеж");
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));
        paymentColumn.setPrefWidth(400);
        paymentColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<Payment, String> unitColumn = new TableColumn<>("Тип");
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        unitColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        unitColumn.setPrefWidth(90);

        TableColumn<Payment, Integer> sumColumn = new TableColumn<>("Сумма");
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        sumColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        sumColumn.setPrefWidth(60);

        table.setItems(paymentObservableList.filtered(item -> item.getType() == PaymentType.TRANSPORTING));
        table.getColumns().addAll(dateColumn, numberColumn, paymentColumn, unitColumn, sumColumn);
        return table;
    }
}
