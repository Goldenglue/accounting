package dataclasses;

public enum StatisticsTypes {
    GENERAL("Общая"), CABINS("Аренда"), PAYMENTS("Платежи"), MECHANISMS("Механизмы");

    public String getType() {
        return type;
    }

    private String type;

    StatisticsTypes(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;

    }
}
