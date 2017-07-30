package dataclasses;

public enum CashType {
    CASH("Наличный расчет"), CASHLESS("Безналичный расчет");

    private String type;

    CashType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}
