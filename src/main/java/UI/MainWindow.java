package UI;

import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;


public class MainWindow {
    private static PaymentTab paymentTab;
    private static RentPaymentsTab rentPaymentsTab;
    private static MechanismsPaymentTab mechanismsPaymentTab;
    private static CabinsPaymentsTab cabinsPaymentsTab;

    static {
        paymentTab = getPaymentsTab();
        rentPaymentsTab = getRentPaymentsTab();
        mechanismsPaymentTab = getMechanismsTab();
        cabinsPaymentsTab = getCabinsPaymentsTab();
    }

    public static void launch(Stage primaryStage) {
        primaryStage.setTitle("Учет");
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(paymentTab, rentPaymentsTab, mechanismsPaymentTab, cabinsPaymentsTab);
        Scene scene = new Scene(tabPane, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static PaymentTab getPaymentsTab() {
        return new PaymentTab();
    }

    private static RentPaymentsTab getRentPaymentsTab() {
        return new RentPaymentsTab();
    }

    private static MechanismsPaymentTab getMechanismsTab() {
        return new MechanismsPaymentTab();
    }

    private static CabinsPaymentsTab getCabinsPaymentsTab() {
        return new CabinsPaymentsTab();
    }

}
