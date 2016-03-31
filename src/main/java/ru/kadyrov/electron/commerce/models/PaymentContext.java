package ru.kadyrov.electron.commerce.models;

public class PaymentContext {

    private Order order;

    public PaymentContext(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
