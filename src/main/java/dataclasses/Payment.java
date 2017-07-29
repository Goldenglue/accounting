package dataclasses;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Payment {
    private final SimpleObjectProperty<LocalDate> date;
    private final SimpleStringProperty payment;
    private final SimpleStringProperty type;
    private final SimpleIntegerProperty sum;
    private SimpleIntegerProperty ID;

    public Payment(LocalDate date, String payment, String type, int sum) {
        this.date = new SimpleObjectProperty<>(date);
        this.payment = new SimpleStringProperty(payment);
        this.type = new SimpleStringProperty(type);
        this.sum = new SimpleIntegerProperty(sum);
        this.ID = new SimpleIntegerProperty(0);
    }

    public Payment(LocalDate date, String payment, String type, int sum, int ID) {
        this.date = new SimpleObjectProperty<>(date);
        this.payment = new SimpleStringProperty(payment);
        this.type = new SimpleStringProperty(type);
        this.sum = new SimpleIntegerProperty(sum);
        this.ID = new SimpleIntegerProperty(ID);
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

    public SimpleStringProperty unitProperty() {
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

}