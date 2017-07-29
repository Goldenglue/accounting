package UI;

import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;


public class MainWindow {
    private static AllPaymentsTab allPaymentsTab;
    private static RentPaymentsTab rentPaymentsTab;
    private static MechanismsPaymentTab mechanismsPaymentTab;
    private static CabinsTab cabinsTab;

    static {
        allPaymentsTab = getPaymentsTab();
        rentPaymentsTab = getRentPaymentsTab();
        mechanismsPaymentTab = getMechanismsTab();
        cabinsTab = getCabinsTab();
    }

    public static void launch(Stage primaryStage) {
        primaryStage.setTitle("Учет");
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(allPaymentsTab, rentPaymentsTab, mechanismsPaymentTab, cabinsTab);
        Scene scene = new Scene(tabPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static AllPaymentsTab getPaymentsTab() {
        return new AllPaymentsTab();
    }

    private static RentPaymentsTab getRentPaymentsTab() {
        return new RentPaymentsTab();
    }

    private static MechanismsPaymentTab getMechanismsTab() {
        return new MechanismsPaymentTab();
    }

    private static CabinsTab getCabinsTab() {
        return new CabinsTab();
    }

}
