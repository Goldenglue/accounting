package dataclasses;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class Renter {
    private SimpleStringProperty renter;
    private SimpleObjectProperty<ArrayList<Cabin>> rentedCabins;
    private SimpleIntegerProperty debtAmount;
}
