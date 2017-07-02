package core;

import javafx.application.Application;
import javafx.stage.Stage;

public class Accounting extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        UI.MainWindow.launch(primaryStage);
    }
}
