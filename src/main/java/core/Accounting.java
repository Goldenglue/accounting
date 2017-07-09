package core;

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

    /*@Override
    public void stop() throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Сохранение изменний");
        alert.setHeaderText(null);
        alert.setContentText("Сохранить изменения?");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> PaymentTab.observableList.forEach(item -> DataProcessing.insertPaymentIntoDatabase(((PaymentTab.Payment) item))));

    }*/
}
