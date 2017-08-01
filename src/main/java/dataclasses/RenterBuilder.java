package dataclasses;

public class RenterBuilder {
    private String renter;
    private Integer[] rentedCabins;
    private Integer debtAmount = 0;
    private String phoneNumber;
    private String email;
    private String info;

    public RenterBuilder setRenter(String renter) {
        this.renter = renter;
        return this;
    }

    public RenterBuilder setRentedCabins(Integer[] rentedCabins) {
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

    public Renter createRenter() {
        return new Renter(renter, rentedCabins, debtAmount, phoneNumber, email, info);
    }
}