package mysite.models;

import java.util.Objects;

public class Payment {

    private Integer id;
    private Order order;
    private Long amount;
    private String expiration;
    private String number;
    private String cvv;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", order=" + order +
                ", amount=" + amount +
                ", expiration='" + expiration + '\'' +
                ", number='" + number + '\'' +
                ", cvv='" + cvv + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id) &&
                Objects.equals(order, payment.order) &&
                Objects.equals(amount, payment.amount) &&
                Objects.equals(expiration, payment.expiration) &&
                Objects.equals(number, payment.number) &&
                Objects.equals(cvv, payment.cvv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, amount, expiration, number, cvv);
    }
}
