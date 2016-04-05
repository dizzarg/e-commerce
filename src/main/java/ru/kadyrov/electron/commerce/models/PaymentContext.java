package ru.kadyrov.electron.commerce.models;

public class PaymentContext {

    private Order order;
    private Long amount;
    private String expiration;
    private String number;
    private String cvv;

    public PaymentContext(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
