package UI;

import database.DataProcessing;
import dataclasses.StatisticsTypes;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Statistics extends Tab {
    HBox hBox = new HBox();

    Statistics() {
        createGUI();
        this.setContent(hBox);
        this.setText("Платежи");
        this.setClosable(false);
    }

    private void createGUI() {
        final ComboBox<StatisticsTypes> typeOfStatistic = new ComboBox<>();
        typeOfStatistic.getItems().addAll(StatisticsTypes.values());
        typeOfStatistic.getSelectionModel().selectFirst();
        typeOfStatistic.setEditable(false);
        typeOfStatistic.setPrefWidth(100);

        final Button chooseType = new Button("Выбрать");
        chooseType.setOnAction(event -> {
            try {
                showRentStatistics();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        hBox.getChildren().addAll(typeOfStatistic, chooseType);
    }

    private void showGeneralStatistics(StatisticsTypes type) {

    }

    private void showRentStatistics() throws SQLException {
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Аренда");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ResultSet set = DataProcessing.getRentStatistics();
        assert set != null;
        List<Label> labels = new ArrayList<>();
        Double totalInUse = null;
        Double totalPaid = null;
        while (set.next()) {
            totalInUse =  ((double)set.getInt(4) / (double)set.getInt(1));
            totalPaid =  ((double)set.getInt(4) / (double)set.getInt(1));
            for (int i = 1; i < 8; i++) {
                labels.add(new Label(String.valueOf(set.getInt(i))));
            }

        }

        int index = 0;

        grid.add(new Label("Всего вагончиков"), 0, index);
        grid.add(labels.get(0), 1, index);

        grid.add(new Label("Максимально возможный размер аренды в месяц"), 0, ++index);
        grid.add(labels.get(1), 1, index);

        grid.add(new Label("Суммарная инвентарная стоимость"), 0, ++index);
        grid.add(labels.get(2), 1, index);

        grid.add(new Label("Вагончиков в аренде"), 0, ++index);
        grid.add(labels.get(3), 1, index);

        grid.add(new Label("Вагончиков свободно"), 0, ++index);
        grid.add(labels.get(4), 1, index);

        grid.add(new Label("Используются в % "),0,++index);
        grid.add(new Label(String.valueOf(totalInUse).substring(0,4)),1,index);

        grid.add(new Label("Вагончиков не оплачено за текущий месяц"), 0, ++index);
        grid.add(labels.get(5), 1, index);

        grid.add(new Label("Вагончиков оплачено за текущий месяц"), 0, ++index);
        grid.add(labels.get(6), 1, index);



        dialog.getDialogPane().setContent(grid);
        dialog.showAndWait();


    }

    private void showMechanismsStatistics() {

    }

    private void showPaymentsStatistics() {

    }
}
