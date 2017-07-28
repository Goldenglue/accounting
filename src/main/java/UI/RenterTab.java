package UI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class RenterTab {
    public static class Renter {
        private SimpleStringProperty renter;
        private SimpleObjectProperty<ArrayList<CabinsTab.Cabin>> rentedCabins;
        private SimpleIntegerProperty debtAmount;
    }
}
