package UI;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.time.LocalDate;

public class MechanismsPaymentTab extends AbstractTab {
    private final HBox hBox = new HBox();
    private final VBox vBox = new VBox();
    private TableColumn<PaymentTab.Payment, LocalDate> dateColumn;
    private TableColumn<PaymentTab.Payment, Integer> numberColumn;
    private TableColumn<PaymentTab.Payment, String> paymentColumn;
    private TableColumn<PaymentTab.Payment, String> unitColumn;
    private TableColumn<PaymentTab.Payment, Integer> sumColumn;

    MechanismsPaymentTab() {

    }

    @Override
    protected TableView setTableUp() {
        TableView<PaymentTab.Payment> table = new TableView<>();
        table.setEditable(false);

        dateColumn = new TableColumn<>("Дата");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setCellFactory(column -> new PaymentTab.LocalDateCellFactory());
        dateColumn.setPrefWidth(100);

        numberColumn = new TableColumn<>("№ п/п");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        numberColumn.setPrefWidth(60);

        paymentColumn = new TableColumn<>("Платеж");
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));
        paymentColumn.setPrefWidth(400);
        paymentColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        unitColumn = new TableColumn<>("Тип");
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        unitColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        unitColumn.setPrefWidth(90);

        sumColumn = new TableColumn<>("Сумма");
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        sumColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        sumColumn.setPrefWidth(60);

        table.setItems(observableList.filtered(item -> item.getType().equals("Аренда")));
        table.getColumns().addAll(dateColumn,numberColumn,paymentColumn,unitColumn,sumColumn);
        return table;
    }

}
