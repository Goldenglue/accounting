package dataclasses;

import java.time.LocalDate;

public class PaymentBuilder {
    private LocalDate date;
    private String payment;
    private PaymentType type;
    private int sum;
    private int id = 0;
    private CashType cashType;

    public PaymentBuilder setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public PaymentBuilder setPayment(String payment) {
        this.payment = payment;
        return this;
    }

    public PaymentBuilder setType(PaymentType type) {
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

    public PaymentBuilder setCashType(CashType cashType) {
        this.cashType = cashType;
        return this;
    }

    public Payment createPayment() {
        return new Payment(date, payment, type, sum, id, cashType);
    }
}