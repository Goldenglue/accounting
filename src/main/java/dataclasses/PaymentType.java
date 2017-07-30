package dataclasses;

public enum PaymentType {
    RENT("Аренда"), TRANSPORTING("Транспортировка");

    private String type;

    PaymentType(String status) {
        this.type = status;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}
