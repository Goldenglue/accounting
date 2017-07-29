package dataclasses;

import java.time.LocalDate;

public class PaymentBuilder {
    private LocalDate date;
    private String payment;
    private String type;
    private int sum;
    private int id = 0;

    public PaymentBuilder setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public PaymentBuilder setPayment(String payment) {
        this.payment = payment;
        return this;
    }

    public PaymentBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public PaymentBuilder setSum(int sum) {
        this.sum = sum;
        return this;
    }

    public PaymentBuilder setID(int id) {
        this.id = id;
        return this;
    }

    public Payment createPayment() {
        return new Payment(date, payment, type, sum, id);
    }
}