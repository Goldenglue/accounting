package core;

import UI.MainWindow;
import UI.PaymentTab;
import database.DataProcessing;
import javafx.application.Application;
import javafx.stage.Stage;

public class Accounting extends Application {

    public static void main(String[] args) {
        DataProcessing.connectToDatabase();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        UI.MainWindow.launch(primaryStage);
    }

    @Override
    public void stop() throws Exception {
        PaymentTab.observableList.forEach(item -> {
            DataProcessing.insertPaymentIntoDatabase(((PaymentTab.Payment) item));
        });
    }
}
