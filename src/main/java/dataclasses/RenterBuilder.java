package dataclasses;

import java.util.ArrayList;

public class RenterBuilder {
    private String renter;
    private ArrayList<Integer> rentedCabins = new ArrayList<>();
    private int debtAmount ;
    private String phoneNumber = "";
    private String email = "";
    private String info = "";
    private int ID;

    public RenterBuilder setRenter(String renter) {
        this.renter = renter;
        return this;
    }

    public RenterBuilder setRentedCabins(ArrayList<Integer> rentedCabins) {
        this.rentedCabins = rentedCabins;
        return this;
    }

    public RenterBuilder setDebtAmount(Integer debtAmount) {
        this.debtAmount = debtAmount;
        return this;
    }

    public RenterBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public RenterBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public RenterBuilder setInfo(String info) {
        this.info = info;
        return this;
    }

    public RenterBuilder setID(Integer ID) {
        this.ID = ID;
        return this;
    }

    public Renter createRenter() {
        return new Renter(renter, rentedCabins, debtAmount, phoneNumber, email, info, ID);
    }
}