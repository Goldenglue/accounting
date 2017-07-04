package core;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Payment {
    private final SimpleStringProperty date;
    private final SimpleIntegerProperty number;
    private final SimpleStringProperty payment;
    private final SimpleStringProperty unit = new SimpleStringProperty("Месяц");
    private final SimpleIntegerProperty amount = new SimpleIntegerProperty(1);
    private final SimpleIntegerProperty price;
    private final SimpleIntegerProperty sum;

    public Payment(String date, int number, String payment, int price, int sum) {
        this.date = new SimpleStringProperty(date);
        this.number = new SimpleIntegerProperty(number);
        this.payment = new SimpleStringProperty(payment);
        this.price = new SimpleIntegerProperty(price);
        this.sum = new SimpleIntegerProperty(sum);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
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

    public String getPayment() {
        return payment.get();
    }

    public SimpleStringProperty paymentProperty() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment.set(payment);
    }

    public String getUnit() {
        return unit.get();
    }

    public SimpleStringProperty unitProperty() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    public int getAmount() {
        return amount.get();
    }

    public SimpleIntegerProperty amountProperty() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    public int getPrice() {
        return price.get();
    }

    public SimpleIntegerProperty priceProperty() {
        return price;
    }

    public void setPrice(int price) {
        this.price.set(price);
    }

    public int getSum() {
        return sum.get();
    }

    public SimpleIntegerProperty sumProperty() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum.set(sum);
    }
}
