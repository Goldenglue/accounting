package UI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableView;

import java.time.LocalDate;

public class RentPaymentsTab extends AbstractTab {

    @Override
    protected TableView setTableUp() {
        return null;
    }

    @Override
    protected void loadFromDatabase() {

    }

    @Override
    protected void createGUI() {

    }

    public static class RentPayment {
        private final SimpleObjectProperty<LocalDate> date;
        private final SimpleIntegerProperty number;
        private final SimpleStringProperty payment;
        private final SimpleStringProperty type;
        private final SimpleIntegerProperty price;
        private final SimpleIntegerProperty sum;

        public RentPayment(LocalDate date, int number, String payment, String type, int price, int sum) {
            this.date = new SimpleObjectProperty<>(date);
            this.number = new SimpleIntegerProperty(number);
            this.payment = new SimpleStringProperty(payment);
            this.type = new SimpleStringProperty(type);
            this.price = new SimpleIntegerProperty(price);
            this.sum = new SimpleIntegerProperty(sum);
        }

        public LocalDate getDate() {
            return date.get();
        }

        public SimpleObjectProperty<LocalDate> dateProperty() {
            return date;
        }

        public void setDate(LocalDate date) {
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

        public String getType() {
            return type.get();
        }

        public SimpleStringProperty typeProperty() {
            return type;
        }

        public void setType(String type) {
            this.type.set(type);
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
}
