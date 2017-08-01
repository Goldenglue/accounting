package dataclasses;

import database.DataProcessing;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import utils.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Cabin {
    private SimpleIntegerProperty ID;
    private SimpleIntegerProperty number;
    private SimpleStringProperty name;
    private SimpleIntegerProperty rentPrice;
    private SimpleIntegerProperty currentPaymentAmount;
    private SimpleIntegerProperty inventoryPrice;
    private SimpleObjectProperty<LocalDate> transferDate;
    private SimpleStringProperty renter;
    private SimpleBooleanProperty isPaid;
    private SimpleObjectProperty<ArrayList<LocalDate>> paymentDates;
    private SimpleStringProperty additionalInfo;
    private SimpleIntegerProperty currentPaymentDate;
    private SimpleObjectProperty<ArrayList<String>> previousRenters;
    private SimpleStringProperty series;
    private SimpleObjectProperty<StockStatus> status;

    public Cabin(int ID, int number, String name, int rentPrice, int currentPaymentAmount, int inventoryPrice, LocalDate transferDate, String renter, boolean isPaid, String[] paymentDates, String additionalInfo, int currentPaymentDate, String[] previousRenters, String series, StockStatus status) {
        this.ID = new SimpleIntegerProperty(ID);
        this.number = new SimpleIntegerProperty(number);
        this.name = new SimpleStringProperty(name);
        this.rentPrice = new SimpleIntegerProperty(rentPrice);
        this.currentPaymentAmount = new SimpleIntegerProperty(currentPaymentAmount);
        this.inventoryPrice = new SimpleIntegerProperty(inventoryPrice);
        this.transferDate = new SimpleObjectProperty<>(transferDate);
        this.renter = new SimpleStringProperty(renter);
        this.isPaid = new SimpleBooleanProperty(isPaid);
        this.isPaid.addListener((observable, oldValue, newValue) -> this.setIsPaid(newValue));
        this.paymentDates = new SimpleObjectProperty<>(new ArrayList<>());
        this.paymentDates.get().addAll(Arrays.stream(paymentDates)
                .filter(integer -> !Objects.equals(integer, "0") && !integer.equals(""))
                .map(s -> LocalDate.parse(s, Utils.formatterOfyyyyMMdd))
                .collect(Collectors.toList()));
        this.additionalInfo = new SimpleStringProperty(additionalInfo);
        this.currentPaymentDate = new SimpleIntegerProperty(currentPaymentDate);
        this.previousRenters = new SimpleObjectProperty<>(new ArrayList<>());
        this.previousRenters.get().addAll(Arrays.asList(previousRenters));
        this.series = new SimpleStringProperty(series);
        this.status = new SimpleObjectProperty<>(status);
    }

    public void payForCabin(LocalDate date) {
        setIsPaid(true);
        getPaymentDates().add(date);
        setCurrentPaymentDate(date.getDayOfMonth());
        DataProcessing.updateCabinPaymentStatus(this);
    }

    public void toStock() {
        DataProcessing.updateCabinStockStatus(this, true);
        setCurrentPaymentAmount(0);
        setTransferDate(null);
        setIsPaid(false);
        setPaymentDates(null);
        setCurrentPaymentDate(0);
        getPreviousRenters().add(getRenter());
        setRenter("");
        setStatus(StockStatus.IN_STOCK);
    }

    public void updateCabin() {
        DataProcessing.updateCabin(this);
    }

    public int getNumber() {
        return number.get();
    }

    public SimpleIntegerProperty numberProperty() {
        return number;
    }

    public void setNumber(int number) {
        this.number.set(number);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getRentPrice() {
        return rentPrice.get();
    }

    public SimpleIntegerProperty rentPriceProperty() {
        return rentPrice;
    }

    public void setRentPrice(int rentPrice) {
        this.rentPrice.set(rentPrice);
    }

    public int getCurrentPaymentAmount() {
        return currentPaymentAmount.get();
    }

    public SimpleIntegerProperty currentPaymentAmountProperty() {
        return currentPaymentAmount;
    }

    public void setCurrentPaymentAmount(int currentPaymentAmount) {
        this.currentPaymentAmount.set(currentPaymentAmount);
    }

    public int getInventoryPrice() {
        return inventoryPrice.get();
    }

    public SimpleIntegerProperty inventoryPriceProperty() {
        return inventoryPrice;
    }

    public void setInventoryPrice(int inventoryPrice) {
        this.inventoryPrice.set(inventoryPrice);
    }

    public LocalDate getTransferDate() {
        return transferDate.get();
    }

    public SimpleObjectProperty<LocalDate> transferDateProperty() {
        return transferDate;
    }

    public void setTransferDate(LocalDate transferDate) {
        this.transferDate.set(transferDate);
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

    public int getCurrentPaymentDate() {
        return currentPaymentDate.get();
    }

    public SimpleIntegerProperty currentPaymentDateProperty() {
        return currentPaymentDate;
    }

    public void setCurrentPaymentDate(int currentPaymentDate) {
        this.currentPaymentDate.set(currentPaymentDate);
    }

    public boolean isIsPaid() {
        return isPaid.get();
    }

    public SimpleBooleanProperty isPaidProperty() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid.set(isPaid);
    }

    public String getSeries() {
        return series.get();
    }

    public SimpleStringProperty seriesProperty() {
        return series;
    }

    public void setSeries(String series) {
        this.series.set(series);
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

    public String getAdditionalInfo() {
        return additionalInfo.get();
    }

    public SimpleStringProperty additionalInfoProperty() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo.set(additionalInfo);
    }

    public ArrayList<LocalDate> getPaymentDates() {
        return paymentDates.get();
    }

    public SimpleObjectProperty<ArrayList<LocalDate>> paymentDatesProperty() {
        return paymentDates;
    }

    public void setPaymentDates(ArrayList<LocalDate> paymentDates) {
        this.paymentDates.set(paymentDates);
    }

    public ArrayList<String> getPreviousRenters() {
        return previousRenters.get();
    }

    public SimpleObjectProperty<ArrayList<String>> previousRentersProperty() {
        return previousRenters;
    }

    public void setPreviousRenters(ArrayList<String> previousRenters) {
        this.previousRenters.set(previousRenters);
    }

    public StockStatus getStatus() {
        return status.get();
    }

    public SimpleObjectProperty<StockStatus> statusProperty() {
        return status;
    }

    public void setStatus(StockStatus status) {
        this.status.set(status);
    }


    @Override
    public String toString() {
        return "Cabin{" +
                "ID=" + ID +
                ", number=" + number +
                ", name=" + name +
                ", rentPrice=" + rentPrice +
                ", currentPaymentAmount=" + currentPaymentAmount +
                ", inventoryPrice=" + inventoryPrice +
                ", transferDate=" + transferDate +
                ", renter=" + renter +
                ", isPaid=" + isPaid +
                ", paymentDates=" + paymentDates +
                ", additionalInfo=" + additionalInfo +
                ", currentPaymentDate=" + currentPaymentDate +
                ", previousRenters=" + previousRenters +
                ", series=" + series +
                ", status=" + status +
                '}';
    }
}
