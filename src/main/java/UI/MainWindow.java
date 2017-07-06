package UI;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;


public class MainWindow {
    public static PaymentTab paymentTab;
    static {
        paymentTab = getPaymentsTab();
    }

    public static void launch(Stage primaryStage) {
        primaryStage.setTitle("Accounting application");
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(paymentTab, getMechanismsTab());
        Scene scene = new Scene(tabPane, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static PaymentTab getPaymentsTab() {
        return new PaymentTab();
    }

    private static Tab getMechanismsTab() {
        Tab tab = new Tab();
        tab.setText("Механизмы");
        tab.setClosable(false);
        return tab;
    }

}
