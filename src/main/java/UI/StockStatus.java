package UI;

public enum StockStatus {
    IN_STOCK("На складе"), NOT_IN_STOCK("В аренде"), ANY("Все");

    private String status;

    StockStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
