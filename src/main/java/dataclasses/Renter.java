package dataclasses;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.Arrays;

public class Renter {
    private SimpleStringProperty renter;
    private SimpleObjectProperty<ArrayList<Integer>> rentedCabins;
    private SimpleIntegerProperty debtAmount;
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty email;
    private SimpleStringProperty info;
    private SimpleIntegerProperty ID;

    Renter(String renter, ArrayList<Integer> rentedCabins, Integer debtAmount, String phoneNumber, String email, String info, Integer ID) {
        this.renter = new SimpleStringProperty(renter);
        this.rentedCabins = new SimpleObjectProperty<>(rentedCabins);
        this.debtAmount = new SimpleIntegerProperty(debtAmount);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.email = new SimpleStringProperty(email);
        this.info = new SimpleStringProperty(info);
        this.ID = new SimpleIntegerProperty(ID);
    }

    public String getRenter() {
        return renter.get();
    }

    public SimpleStringProperty renterProperty() {
        return renter;
    }

    public void setRenter(String renter) {
        this.renter.set(renter);
    }

    public ArrayList<Integer> getRentedCabins() {
        return rentedCabins.get();
    }

    public SimpleObjectProperty<ArrayList<Integer>> rentedCabinsProperty() {
        return rentedCabins;
    }

    public void setRentedCabins(ArrayList<Integer> rentedCabins) {
        this.rentedCabins.set(rentedCabins);
    }

    public int getDebtAmount() {
        return debtAmount.get();
    }

    public SimpleIntegerProperty debtAmountProperty() {
        return debtAmount;
    }

    public void setDebtAmount(int debtAmount) {
        this.debtAmount.set(debtAmount);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getInfo() {
        return info.get();
    }

    public SimpleStringProperty infoProperty() {
        return info;
    }

    public void setInfo(String info) {
        this.info.set(info);
    }

    public int getID() {
        return ID.get();
    }

    public SimpleIntegerProperty IDProperty() {
        return ID;
    }

    public void setID(int ID) {
        this.ID.set(ID);
    }
}
