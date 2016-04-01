package ru.kadyrov.electron.commerce.models;

import java.util.Objects;



public class Payment {

    private Integer id;
    private Order order;
    private Integer amount;
    private Status status;
    private AccountTransaction transaction;

    /**
     * <ul>
     *  <li>Ожидает оплату</li>
     *  <li>Готов к отправке</li>
     *  <li>Отправлен</li>
     *  <li>Платеж зачислен</li>
     *  <li>Отменен, деньги возвращены</li>
     *  <li>Неверный номер</li>
     *  <li>Ошибка платежа</li>
     *  <li>Платеж оплачен</li>
     *  <li>Платеж возвращен</li>
     *  <li>В процессе оплаты</li>
     * </ul>
     */
    public enum Status {
        WAITTING("Ожидает оплату"),
        READY("Готов к отправке"),
        SENDED("Отправлен"),
        CREDITED("Платеж зачислен"),
        CANSELED("Отменен, деньги возвращены"),
        INVALID_NUMBER("Неверный номер"),
        ERROR("Ошибка платежа"),
        PAID("Платеж оплачен"),
        RETURNED("Платеж возвращен"),
        INPROGRESS("В процессе оплаты");

        private final String message;

        Status(String s) {
            this.message = s;
        }

        @Override
        public String toString() {
            return message;
        }
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", order=" + order +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id) &&
                Objects.equals(order, payment.order) &&
                Objects.equals(amount, payment.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, amount);
    }
}
