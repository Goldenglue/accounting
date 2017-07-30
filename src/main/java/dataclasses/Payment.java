package dataclasses;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Payment {
    private SimpleObjectProperty<LocalDate> date;
    private SimpleStringProperty payment;
    private SimpleStringProperty type;
    private SimpleIntegerProperty sum;
    private SimpleIntegerProperty ID;
    private SimpleObjectProperty<CashType> cashType;

    Payment(LocalDate date, String payment, String type, int sum, int ID, CashType cashType) {
        this.date = new SimpleObjectProperty<>(date);
        this.payment = new SimpleStringProperty(payment);
        this.type = new SimpleStringProperty(type);
        this.sum = new SimpleIntegerProperty(sum);
        this.ID = new SimpleIntegerProperty(ID);
        this.cashType = new SimpleObjectProperty<>(cashType);
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

    public LocalDate getDate() {
        return date.getValue();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public String getPayment() {
        return payment.get();
    }

    public void setPayment(String payment) {
        this.payment.set(payment);
    }

    public SimpleStringProperty paymentProperty() {
        return payment;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String unit) {
        this.type.set(unit);
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public int getSum() {
        return sum.get();
    }

    public void setSum(int sum) {
        this.sum.set(sum);
    }

    public SimpleIntegerProperty sumProperty() {
        return sum;
    }

    public CashType getCashType() {
        return cashType.get();
    }

    public SimpleObjectProperty<CashType> cashTypeProperty() {
        return cashType;
    }

    public void setCashType(CashType cashType) {
        this.cashType.set(cashType);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "date=" + date +
                ", payment=" + payment +
                ", type=" + type +
                ", sum=" + sum +
                ", ID=" + ID +
                ", cashType=" + cashType +
                '}';
    }
}