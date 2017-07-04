package UI;

import core.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicInteger;


public class MainWindow {
    static ObservableList<Payment> data;

    public static void launch(Stage primaryStage) {
        primaryStage.setTitle("Accounting application");
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(getPaymentsTab(), getMechanismsTab());
        Scene scene = new Scene(tabPane, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static Tab getPaymentsTab() {
        data = FXCollections.observableArrayList();
        data.add(new Payment("02.07.17",  101, "040-05 ООО \"СТС\" у Валеры", 6300, 6300));

        AtomicInteger integer = new AtomicInteger();
        integer.incrementAndGet();
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
}
