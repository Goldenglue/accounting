package core;

import database.DataProcessing;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDate;

public class Accounting extends Application {

    public static void main(String[] args) {
        try {
            DataProcessing.connectToDatabase();
            DataProcessing.createTableBasedOnLocalDate(LocalDate.now());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        UI.MainWindow.launch(primaryStage);
    }

    @Override
    public void stop() throws Exception {
        DataProcessing.backupDatabase();
        System.out.println("Backup created");
    }
}
