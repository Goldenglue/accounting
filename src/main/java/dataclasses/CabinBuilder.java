package dataclasses;

import java.time.LocalDate;

public class CabinBuilder {
    private int id = 0;
    private int number;
    private String name;
    private int rentPrice;
    private int currentPaymentAmount;
    private int inventoryPrice;
    private LocalDate transferDate;
    private String renter;
    private boolean isPaid;
    private String[] paymentDates;
    private String additionalInfo;
    private int currentPaymentDate;
    private String[] previousRenters;
    private String series;

    public CabinBuilder setID(int id) {
        this.id = id;
        return this;
    }

    public CabinBuilder setNumber(int number) {
        this.number = number;
        return this;
    }

    public CabinBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public CabinBuilder setRentPrice(int rentPrice) {
        this.rentPrice = rentPrice;
        return this;
    }

    public CabinBuilder setCurrentPaymentAmount(int currentPaymentAmount) {
        this.currentPaymentAmount = currentPaymentAmount;
        return this;
    }

    public CabinBuilder setInventoryPrice(int inventoryPrice) {
        this.inventoryPrice = inventoryPrice;
        return this;
    }

    public CabinBuilder setTransferDate(LocalDate transferDate) {
        this.transferDate = transferDate;
        return this;
    }

    public CabinBuilder setRenter(String renter) {
        this.renter = renter;
        return this;
    }

    public CabinBuilder setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
        return this;
    }

    public CabinBuilder setPaymentDates(String[] paymentDates) {
        this.paymentDates = paymentDates;
        return this;
    }

    public CabinBuilder setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    public CabinBuilder setCurrentPaymentDate(int currentPaymentDate) {
        this.currentPaymentDate = currentPaymentDate;
        return this;
    }

    public CabinBuilder setPreviousRenters(String[] previousRenters) {
        this.previousRenters = previousRenters;
        return this;
    }

    public CabinBuilder setSeries(String series) {
        this.series = series;
        return this;
    }

    public Cabin createCabin() {
        return new Cabin(id, number, name, rentPrice, currentPaymentAmount, inventoryPrice, transferDate, renter, isPaid, paymentDates, additionalInfo, currentPaymentDate, previousRenters, series);
    }
}