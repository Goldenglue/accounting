package UI;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class RentPaymentsTab extends AbstractTab {
    private final HBox hBox = new HBox();
    private final VBox vBox = new VBox();
    private TableColumn<PaymentTab.Payment, LocalDate> dateColumn;
    private TableColumn<PaymentTab.Payment, Integer> numberColumn;
    private TableColumn<PaymentTab.Payment, String> paymentColumn;
    private TableColumn<PaymentTab.Payment, String> unitColumn;
    private TableColumn<PaymentTab.Payment, Integer> priceColumn;
    private TableColumn<PaymentTab.Payment, Integer> sumColumn;

    RentPaymentsTab() {
    }


    @Override
    protected TableView<PaymentTab.Payment> setTableUp() {
        return null;
    }

    @Override
    protected void loadFromDatabase() {

    }

    @Override
    protected void createGUI() {

    }

}
